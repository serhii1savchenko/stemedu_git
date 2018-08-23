/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.mathpar.students.ukma17i41.bosa.parallel.engine;
package com.mathpar.parallel.dap;

import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

public class DispThread {
    Pine firtree;
    long sleepTime;
    long executeTime;
    Integer myRank;
    CalcThread counter;
    static int mode = 0;
    static boolean iAmFreeAndSentToParent = false;
    TreeSet<Integer> freeProcs;
    Map<Integer, int[]> dNodes;//!!!!map?
    Map<Integer, ArrayList<Integer>> pNodes;
    int flagOfMyState = 0;

    // теги:
    //     0: сообщение содержит задачу, отосланую процесору из списка свободних 
    //     1: сообщение содержит свободные узлы
    //     2: сообщение содержит состояние процесора
    //     3: сообщение содержит результат задачи
    //     4: сообщение содержит задачу, отосланую процесору из списка дочерних 
    //     5: сообщение содержит команду на завершение (вся задача посчитана)
    public DispThread(int startType, Pine f, long sTime, String[] args, Element[] data, Ring ring) throws MPIException {
        firtree = f;
        sleepTime = sTime;
        executeTime = 0;
        myRank = MPI.COMM_WORLD.getRank();
        freeProcs = new TreeSet<Integer>();
        dNodes = new HashMap<Integer, int[]>();
        pNodes = new HashMap<Integer, ArrayList<Integer>>();

        try {
            int all = MPI.COMM_WORLD.getSize();
            int[] nodes = new int[all];
            for (int i = 0; i < all; i++) {
                nodes[i] = i;
            }
            execute(startType, args, data, nodes, ring);
        } catch (Exception e) {
        }

    }

    public long GetExecuteTime() {
        return executeTime;
    }

    private void rootWork(int[] nodes, int startType, Element[] data) {
        System.out.println("i'm root");
        for (int i = 1; i < nodes.length; i++) {
            freeProcs.add(nodes[i]);
        }

        Object[] mas = new Object[] {Tools.doNewDrop(startType, 0, 0, 0, 0, data)};//!!!!-1-1-1-1
        counter.putDropInVokzal(mas);

    }

    private void exit() {
        System.out.println("--------------------------------5");
        mode = -1;
        System.out.println("вся задача посчитана, myrank is   " + myRank);
        counter.DoneThread();
    }

    private void receiveTask(int cnt, int tag) throws MPIException {
        System.out.println("--------------------------------0");
        System.out.println("режим ожидания задачи-- ");

        Object[] tmpAr = new Object[cnt];
        tmpAr = Tools.recvObjects(cnt, MPI.ANY_SOURCE, tag);
        System.out.println("Recieved task, myrank is   " + myRank);

        counter.putDropInVokzal(tmpAr);//!!
    }

    private void receiveFreeProcs(int cnt) throws MPIException {
        System.out.println("--------------------------------1");
        //получено сообщение от род. узла со своб. процессами

        Object[] freeProcsAr = new Object[cnt];
        freeProcsAr = Tools.recvObjects(cnt, MPI.ANY_SOURCE, 1);
        System.out.println("Recieved свободные узлы, myrank is   " + myRank);
        for (int i = 0; i < cnt; i++) {
            freeProcs.add((int) freeProcsAr[i]);
        }

        if (freeProcs.contains(myRank)) {//?
            freeProcs.remove(myRank);
        }
    }

    private void procState(int cnt, Integer daughter) throws MPIException {
        int[] tmr = new int[cnt];
        MPI.COMM_WORLD.recv(tmr, cnt, MPI.INT, daughter, 2);
        System.out.println("Recieved состояние процесора, myrank is   " + myRank);
        dNodes.get(daughter)[0] = tmr[0];
    }

    private void endProgramme(int cntProc, Object[] tmp, int[] nodes) throws MPIException {
        System.out.println("daughter == myRank   ");
        for (int i = 0; i < ((Element[]) tmp[0]).length; i++) {
            System.out.println("RESULT = " + ((Element[]) tmp[0])[i]);
        }
        int[] tmpAr = {24};
        for (int j = 1; j < cntProc; j++) {
            MPI.COMM_WORLD.send(tmpAr, 1, MPI.INT, nodes[j], 5);
        }
        mode = -1;
        counter.DoneThread();

    }

    private void deleteDaughter(Integer daughter, Object[] tmp) {
        if (dNodes.get(daughter).length == 3) {
            dNodes.remove(daughter);
        } else {
            int index = -1;

            for (int j = 1; j < dNodes.get(daughter).length; j += 2) {
                if (dNodes.get(daughter)[j] == (int) tmp[1] && dNodes.get(daughter)[j + 1] == (int) tmp[2]) {
                    index = j;
                    break;
                }
            }
            int[] newMas = new int[dNodes.get(daughter).length - 2];
            System.arraycopy(dNodes.get(daughter), 0, newMas, 0, index);
            System.arraycopy(dNodes.get(daughter), index + 2, newMas, index, dNodes.get(daughter).length - (index + 2));
            dNodes.put(daughter, newMas);
        }
    }

    private void receiveResult(Integer daughter, int cntProc, int[] nodes, Ring ring) throws MPIException {
        Object[] tmp = new Object[3];
        tmp = Tools.recvObjects(3, daughter, 3);
        System.out.println("Recieved результат задачи, myrank is   " + myRank);
        boolean isReadyF = false;
        Amin amin = null;
        if (daughter == myRank) {
            //задача посчитана в корневом узле - завершение работы
            endProgramme(cntProc, tmp, nodes);
        } else {

            DropTask currentDrop = firtree.body.get((int) tmp[1]).branch.get((int) tmp[2]);

            currentDrop.outData = (Element[]) tmp[0];
            currentDrop.state = 2;

            deleteDaughter(daughter, tmp);

            isReadyF = counter.writeResultsToAmin(currentDrop);
            amin = firtree.body.get((int) tmp[1]);

            amin = counter.writeAllResultsInFirtree(amin, isReadyF);

            //задача посчитана - перейти в режим 2
            if (myRank == nodes[0] && Tools.isEmptyArray(counter.vokzal) && firtree.body.get(0).aminState == 2) {
                endProgramme(cntProc, tmp, nodes);
            }

            if (amin.parentAmin != myRank) {
                Object[] res = {amin.outputData, (Integer) amin.parentAmin, (Integer) amin.parentDrop};
                Tools.sendObjects(res, amin.parentProc, 3);
                
                pNodes.get(amin.parentProc).remove((Integer) amin.aminIdInFirtree);

                if (pNodes.get(amin.parentProc).isEmpty()) {
                    pNodes.remove((Integer) amin.parentProc);
                }

                System.out.println("Sending result (disp) from  " + myRank + " to " + amin.parentProc);
            }
        }

    }

    private int getIndex() {
        for (int i = 0; i < counter.vokzal.length; i++) {
            if (counter.vokzal[i] != null && !counter.vokzal[i].isEmpty()) {
                return i;
            }
        }
        return 0;
    }

    private void addDaugter(int daughtProcs, int curDrop, int curAmin) {
        if (!dNodes.keySet().contains(daughtProcs)) {
            int[] history = new int[] {0, curAmin, curDrop};
            dNodes.put(daughtProcs, history);
        } else {
            int[] newMas = new int[dNodes.get(daughtProcs).length + 2];
            System.arraycopy(dNodes.get(daughtProcs), 0, newMas, 0, dNodes.get(daughtProcs).length);
            newMas[newMas.length - 1] = curDrop;
            newMas[newMas.length - 1] = curAmin;
            dNodes.put(daughtProcs, newMas);
        }

    }

    private void sendDropsAndFreeProcsToDaughers(Object[] procs, int tag, boolean fromFree) throws MPIException {

        System.out.println("--------------------------------111111");

        int index = getIndex();
        int number = procs.length / counter.vokzal[index].size() + 1;

        for (int i = 0; i < counter.vokzal[index].size(); i++) {

            DropTask curTask = counter.vokzal[index].get(i);
            int curAmin = curTask.aminId;
            int curDrop = curTask.dropId;
            Object[] tmpS = {curTask};

            if (number <= 1) {

                int daughter = (int) procs[i];
                Tools.sendObjects(tmpS, daughter, tag);

                curTask.numberOfDaughterProc = daughter;
                if (fromFree) {
                    freeProcs.remove(daughter);
                }
                addDaugter(daughter, curDrop, curAmin);
            } else {

                int[] daughtProcs = new int[number];
                for (int j = 0; j < number; j++) {
                    daughtProcs[j] = (int) procs[j];
                    if (fromFree) {
                        freeProcs.remove(procs[j]);
                    }
                }

                Tools.sendObjects(tmpS, daughtProcs[0], tag);

                int[] daughtFreeProcs = new int[daughtProcs.length - 1];
                System.arraycopy(daughtProcs, 1, daughtFreeProcs, 0, daughtFreeProcs.length);

                MPI.COMM_WORLD.send(daughtFreeProcs, daughtFreeProcs.length, MPI.INT, daughtProcs[0], 1);

                curTask.numberOfDaughterProc = daughtProcs[0];
                addDaugter(daughtProcs[0], curDrop, curAmin);

            }
            counter.vokzal[index].remove(i);
        }

    }

    private void sendFreeProcsToDaughersNoTask() throws MPIException {
        System.out.println("--------------------------------2222");

        freeProcs.add(myRank);

        ArrayList<Integer> daughterInNeed = new ArrayList();

        for (Object o : dNodes.keySet()) {
            if (dNodes.get(o)[0] == 0) {
                daughterInNeed.add((Integer) o);
            }
        }

        if (freeProcs.size() > daughterInNeed.size()) {
            int procNum = freeProcs.size() / daughterInNeed.size();

            for (int j = 0; j < daughterInNeed.size(); j++) {
                int[] procsToSend = new int[procNum];

                for (int i = 0; i < procNum; i++) {
                    procsToSend[i] = (int) freeProcs.toArray()[i];
                    freeProcs.remove(i);
                }

                MPI.COMM_WORLD.send(procsToSend, procsToSend.length, MPI.INT, daughterInNeed.get(j), 1);
                System.out.println("Sending freeProc from  " + myRank + " to " + daughterInNeed.get(j));
            }
        } else {
            for (int i = 0; i < freeProcs.size(); i++) {
                int[] tmpS = {(int) freeProcs.toArray()[i]};
                MPI.COMM_WORLD.send(tmpS, tmpS.length, MPI.INT, daughterInNeed.get(i), 1);
                System.out.println("Sending freeProc from  " + myRank + " to " + daughterInNeed.get(i));
            }
        }

    }

    private void sendFreeProcsToParent() throws MPIException {
        System.out.println("HI----------------------------3333");

        int parent = (int) pNodes.keySet().toArray()[0];

        freeProcs.add(myRank);
        MPI.COMM_WORLD.send(freeProcs, freeProcs.size(), MPI.INT, parent, 1);
        System.out.println("Sending freeProcs from  " + myRank + " to " + parent);

    }

    private boolean isDaughterFree() {

        for (Object o : dNodes.keySet()) {
            if (dNodes.get(o)[0] == 0) {
                return false;
            }
        }
        return true;
    }

    private void sendState() throws MPIException {
        int curState = isDaughterFree() && Tools.isEmptyArray(counter.vokzal) ? 1 : 0;

        if (curState != flagOfMyState) {
            flagOfMyState = curState;

            int[] state = {flagOfMyState};

            for (Object o : pNodes.keySet()) {
                MPI.COMM_WORLD.send(state, state.length, MPI.INT, (int) o, 2);
                System.out.println("Sending state " + flagOfMyState + " from " + myRank + " to " + (int) o);
            }
        }

    }

    public void execute(int startType, String[] args, Element[] data, int[] nodes, Ring ring) throws mpi.MPIException, InterruptedException, IOException, ClassNotFoundException {

        //System.out.println("In execute, myrank is   " + myRank);
        int cntProc = nodes.length;

        Thread disp = Thread.currentThread();
        disp.setPriority(9);
        disp.setName("disp");

        counter = new CalcThread(firtree, pNodes, ring);

        int rootNumb = nodes[0];

        if (myRank == rootNumb) {
            rootWork(nodes, startType, data);
        }
        executeTime = System.currentTimeMillis();
        if (myRank == rootNumb) {
            System.out.println("DDP start with total nodes=" + cntProc);
            System.out.println("sleep time = " + sleepTime);
        }

        while (mode != -1) {

            Status info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, MPI.ANY_TAG);

            if (info != null) {
                int tag = info.getTag();
                int cnt = info.getCount(MPI.INT);
                Integer daughter;
                System.out.println("^^^^^^^^^^^tag = " + tag);

                if (tag == 5) {
                    exit();
                }
                if (tag == 0) {
                    //режим ожидания задачи
                    cnt = info.getCount(MPI.INT);
                    receiveTask(cnt, 0);
                    iAmFreeAndSentToParent = false;
                }
                if (tag == 1) {
                    //получение свободных процессов от род. узла:
                    while (info != null) {
                        cnt = info.getCount(MPI.INT);
                        receiveFreeProcs(cnt);
                        info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 1);
                    }
                }
                if (tag == 2) {

                    //состояние процесора
                    while (info != null) {
                        System.out.println("--------------------------------2");
                        cnt = info.getCount(MPI.INT);
                        daughter = info.getSource();

                        procState(cnt, daughter);

                        info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 2);
                    }
                }
                if (tag == 3) {

                    //результат 
                    while (info != null) {
                        System.out.println("--------------------------------3");
                        daughter = info.getSource();

                        receiveResult(daughter, cntProc, nodes, ring);

                        info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 3);
                    }
                }
                if (tag == 4) {
                    //режим ожидания задачи
                    receiveTask(cnt, 4);
                }
            }

            //Отправка дропов и свободних процесоров дочерним в 2 етапа, если есть задания
            if (freeProcs.size() > 0 && !Tools.isEmptyArray(counter.vokzal)) {

                sendDropsAndFreeProcsToDaughers(freeProcs.toArray(), 0, true);

                if (freeProcs.isEmpty() && !Tools.isEmptyArray(counter.vokzal)) {
                    ArrayList<Integer> daughterFree = new ArrayList();

                    for (Object o : dNodes.keySet()) {
                        if (dNodes.get(o)[0] == 1) {
                            daughterFree.add((Integer) o);
                        }
                    }

                    if (!daughterFree.isEmpty()) {
                        sendDropsAndFreeProcsToDaughers(daughterFree.toArray(), 4, false);
                    }
                }
            }

            //Отправка свободних процесоров дочерним, если я заканачил и нет больше заданий
            if (freeProcs.size() > 0 && Tools.isEmptyArray(counter.vokzal) && dNodes.size() > 0) {
                sendFreeProcsToDaughersNoTask();
            }

            // Отправка родителю свободних процесоров, если нет доступних заданий и нет дочерних
            if (!iAmFreeAndSentToParent && !pNodes.isEmpty() && isDaughterFree() && Tools.isEmptyArray(counter.vokzal)) {
                sendFreeProcsToParent();
                iAmFreeAndSentToParent = true;
            }

            sendState();

            //периодичность диспетчера
            disp.sleep(sleepTime);
            //System.out.println("#After sleep! myRank " + myRank);

        }

        counter.thread.join();
        executeTime = System.currentTimeMillis() - executeTime;
        if (myRank == rootNumb) {
            System.out.println("DAP done.");
            System.out.println("executeTime = " + executeTime);
        }

    }

}
