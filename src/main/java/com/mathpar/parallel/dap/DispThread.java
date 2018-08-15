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

    public DispThread(int startType, Pine f, long sTime, String[] args, Element[] data, Ring ring) throws MPIException {
        firtree = f;
        sleepTime = sTime;
        executeTime = 0;
        myRank = MPI.COMM_WORLD.getRank();

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

    private void rootWork(TreeSet<Integer> freeProcs, int[] nodes, int startType, Element[] data) {
        System.out.println("i'm root");
        for (int i = 1; i < nodes.length; i++) {
            freeProcs.add(nodes[i]);
        }
        Object[] mas = new Object[] {startType, 0, 0, 0, data};//!!!!-1-1-1-1
        counter.putDropInVokzal(mas);

    }

    private void exit() {
        System.out.println("--------------------------------5");
        mode = -1;
        System.out.println("вся задача посчитана, myrank is   " + myRank);
        counter.DoneThread();
    }

    private void receiveTask(TreeSet<Integer> freeProcs) throws MPIException {
        System.out.println("--------------------------------0");
        System.out.println("режим ожидания задачи-- ");

        Object[] tmpAr = new Object[5];
        tmpAr = Tools.recvObjects(5, MPI.ANY_SOURCE, 0);
        System.out.println("Recieved task, myrank is   " + myRank);

        counter.putDropInVokzal(tmpAr);//!!
    }

    private void receiveFreeProcs(TreeSet<Integer> freeProcs, int cnt) throws MPIException {
        System.out.println("--------------------------------1");
        //получено сообщение от род. узла со своб. процессами

        Object[] freeProcsAr = new Object[cnt];
        freeProcsAr = Tools.recvObjects(cnt, MPI.ANY_SOURCE, 1);
        System.out.println("Recieved свободные узлы, myrank is   " + myRank);
        for (int i = 0; i < cnt; i++) {
            freeProcs.add((int) freeProcsAr[i]);
        }

        if (freeProcs.contains(myRank)) {
            freeProcs.remove(myRank);
        }
    }

    private void procState(Map<Integer, int[]> dNodes, int cnt, Integer daughter) throws MPIException {
        int[] tmr = new int[cnt];
        MPI.COMM_WORLD.recv(tmr, cnt, MPI.INT, daughter, 2);
        System.out.println("Recieved состояние процесора, myrank is   " + myRank);
        dNodes.get(daughter)[0] = tmr[0];

        firtree.body.get(tmr[1]).branch.get(tmr[2]).state = tmr[0];

        if (Tools.isEmptyArray(counter.vokzal) && firtree.CheckState() == 1) {
            firtree.SetState(1);
        }

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

    private boolean writeNotMainCompToAmin(Object[] tmp, Amin amin) {
        boolean isReadyF = false;
        {
            Avariya:
            for (int j = 0; j < firtree.body.size(); j++) {
                if (firtree.body.get(j).parentProc == (int) tmp[3] && firtree.body.get(j).parentAmin == (int) tmp[1]
                        && firtree.body.get(j).parentDrop == (int) tmp[2]) {
                    int[] mas = Tools.getDropObject(firtree.body.get(j).type).connectionsOfNotMain;
                    for (int k = 0; k < mas.length; k++) {
                        firtree.body.get(j).resultForOutFunction[mas[k]] = ((Element[]) tmp[0])[k];

                    }
                    amin = firtree.body.get(j);
                    isReadyF = true;
                    break Avariya;
                }

            }
            System.out.println("Не нашлось в елке процесора!!!");
        }

        return isReadyF;

    }

    private void deleteDaughter(Map<Integer, int[]> dNodes, Integer daughter, TreeSet<Integer> freeProcs, Object[] tmp) {
        if (dNodes.get(daughter).length == 3) {
            freeProcs.add(daughter);
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

    private void receiveResult(TreeSet<Integer> freeProcs, Map<Integer, int[]> dNodes, Integer daughter, int cntProc, int[] nodes, Ring ring) throws MPIException {
        Object[] tmp = new Object[4];
        tmp = Tools.recvObjects(4, daughter, 3);
        System.out.println("Recieved результат задачи, myrank is   " + myRank);
        boolean isReadyF = false;
        Amin amin = null;
        if (daughter == myRank) {
            //задача посчитана в корневом узле - завершение работы
            endProgramme(cntProc, tmp, nodes);
        } else {
            if ((int) tmp[3] != -1) {
                isReadyF = writeNotMainCompToAmin(tmp, amin);
            } else {

                DropTask currentDrop = firtree.body.get((int) tmp[1]).branch.get((int) tmp[2]);

                currentDrop.outData = (Element[]) tmp[0];
                currentDrop.state = 2;

                deleteDaughter(dNodes, daughter, freeProcs, tmp);

                isReadyF = writeResultsToAmin(currentDrop, (int) tmp[1], (int) tmp[2]);
                amin = firtree.body.get((int) tmp[1]);
            }

            counter.writeAllResultsInFirtree(amin, isReadyF);

            //задача посчитана - перейти в режим 2
            if (amin.parentAmin == -1) {//!!!!!!!!
                endProgramme(cntProc, tmp, nodes);
            }

            if (amin.parentAmin != myRank) {
                Object[] res = {amin.outputData, (Integer) amin.parentAmin, (Integer) amin.parentDrop};
                Tools.sendObjects(res, amin.parentProc, 3);
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

    private void addDaugter(Map<Integer, int[]> dNodes, int daughtProcs, int curDrop, int curAmin) {
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

    private void sendDropsAndFreeProcsToDaughers(TreeSet<Integer> freeProcs, Map<Integer, int[]> dNodes) throws MPIException {

        System.out.println("--------------------------------111111");

        int index = getIndex();
        int number = freeProcs.size() / counter.vokzal[index].size() + 1;

        for (int i = 0; i < counter.vokzal[index].size(); i++) {

            DropTask curTask = counter.vokzal[index].get(i);
            int curAmin = curTask.aminId;
            int curDrop = firtree.body.get(curAmin).branch.indexOf(curTask);
            Object[] tmpS = {curTask.GetType(), myRank, curAmin, curDrop, curTask.inData};

            if (number <= 1) {

                int daughter = (int) freeProcs.toArray()[i];
                Tools.sendObjects(tmpS, daughter, 0);
                System.out.println("Sending task from  " + myRank + " to " + daughter);
                curTask.numberOfDaughterProc = daughter;
                freeProcs.remove(i);
                addDaugter(dNodes, daughter, curDrop, curAmin);
            } else {

                int[] daughtProcs = new int[number];
                for (int j = 0; j < number; j++) {
                    daughtProcs[j] = (int) freeProcs.toArray()[j];
                    freeProcs.remove(j);
                }

                Tools.sendObjects(tmpS, daughtProcs[0], 0);
                System.out.println("Sending task from  " + myRank + " to " + daughtProcs[0]);

                int[] daughtFreeProcs = new int[daughtProcs.length - 1];
                System.arraycopy(daughtProcs, 1, daughtFreeProcs, 0, daughtFreeProcs.length);
                MPI.COMM_WORLD.send(daughtFreeProcs, daughtFreeProcs.length, MPI.INT, daughtProcs[0], 1);
                System.out.println("Sending freeProcs from  " + myRank + " to " + daughtProcs[0]);

                curTask.numberOfDaughterProc = daughtProcs[0];
                addDaugter(dNodes, daughtProcs[0], curDrop, curAmin);

            }
            counter.vokzal[index].remove(i);
        }

    }

    private void sendFreeProcsToDaughersNoTask(TreeSet<Integer> freeProcs, Map<Integer, int[]> dNodes) throws MPIException {
        System.out.println("--------------------------------2222");

        freeProcs.add(myRank);

        ArrayList<Integer> daughterInNeed = new ArrayList();

        for (Object o : dNodes.keySet()) {
            if (dNodes.get(o).equals(0)) {
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

    private void sendFreeProcsToParent(TreeSet<Integer> freeProcs) throws MPIException {
        System.out.println("--------------------------------3333");
        freeProcs.add(myRank);
        int parent = 0;
        for (int i = 0; i < firtree.body.size(); i++) {
            if (firtree.body.get(i) != null && firtree.body.get(i).parentProc != myRank) {
                parent = firtree.body.get(i).parentProc;
                break;
            }
        }
        MPI.COMM_WORLD.send(freeProcs, freeProcs.size(), MPI.INT, parent, 1);
        System.out.println("Sending freeProcs from  " + myRank + " to " + parent);

    }

    public void execute(int startType, String[] args, Element[] data, int[] nodes, Ring ring) throws mpi.MPIException, InterruptedException, IOException, ClassNotFoundException {

        //System.out.println("In execute, myrank is   " + myRank);
        int cntProc = nodes.length;

        Thread disp = Thread.currentThread();
        disp.setPriority(9);
        disp.setName("disp");

        TreeSet<Integer> freeProcs = new TreeSet<Integer>();
        Map<Integer, int[]> dNodes = new HashMap<Integer, int[]>();

        counter = new CalcThread(firtree, ring);

        int rootNumb = nodes[0];

        if (myRank == rootNumb) {
            rootWork(freeProcs, nodes, startType, data);
        }
        executeTime = System.currentTimeMillis();
        if (myRank == rootNumb) {
            System.out.println("DDP start with total nodes=" + cntProc);
            System.out.println("sleep time = " + sleepTime);
        }

        while (mode != -1) {
            // System.out.println("in while!!  myrank = " +myRank);
            // теги:
            //     0: сообщение содержит задачу
            //     1: сообщение содержит свободные узлы
            //      2: сообщение содержит состояние процесора
            //      3: сообщение содержит результат задачи
            //     5: сообщение содержит команду на завершение (вся задача посчитана)

            Status info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, MPI.ANY_TAG);

            if (info != null) {
                int tag = info.getTag();
                System.out.println("^^^^^^^^^^^tag = " + tag);

                if (tag == 5) {
                    exit();
                }
                if (tag == 0) {
                    //режим ожидания задачи
                    receiveTask(freeProcs);
                }
                if (tag == 1) {
                    int cnt;
                    //получение свободных процессов от род. узла:
                    while (info != null) {
                        cnt = info.getCount(MPI.INT);
                        receiveFreeProcs(freeProcs, cnt);
                        info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 1);
                    }
                }
                if (tag == 2) {
                    int cnt;
                    Integer daughter;
                    //состояние процесора
                    while (info != null) {
                        System.out.println("--------------------------------2");
                        cnt = info.getCount(MPI.INT);
                        daughter = info.getSource();

                        procState(dNodes, cnt, daughter);

                        info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 2);
                    }
                }
                if (tag == 3) {

                    //результат 
                    while (info != null) {
                        System.out.println("--------------------------------3");
                        Integer daughter = info.getSource();

                        receiveResult(freeProcs, dNodes, daughter, cntProc, nodes, ring);

                        info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 3);
                    }
                }
            }

            //Отправка дропов і свободних процесоров, если есть задания
            if (freeProcs.size() > 0 && !Tools.isEmptyArray(counter.vokzal)) {
                sendDropsAndFreeProcsToDaughers(freeProcs, dNodes);
            }

            //Отправка свободних процесоров дочерним, если я заканачил и нет больше заданий
            if (freeProcs.size() > 0 && Tools.isEmptyArray(counter.vokzal) && dNodes.size() > 0) {
                sendFreeProcsToDaughersNoTask(freeProcs, dNodes);
            }

            // Отправка родителю свободних процесоров, если нет доступних заданий и нет дочерних
            if (dNodes.size() == 0 && Tools.isEmptyArray(counter.vokzal)) {
                sendFreeProcsToParent(freeProcs);

            }

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

    public boolean writeResultsToAmin(DropTask drop, int aminId, int dropId) throws MPIException {

        boolean isReadyOutputFunction = true;
        for (int i = 0; i < drop.arcs[dropId + 1].length; i += 3) {
            int number = drop.arcs[dropId + 1][i];

            int from = drop.arcs[number][i + 1];
            int to = drop.arcs[number][i + 2];
            if (drop.arcs[number] != null) {
                isReadyOutputFunction = false;
                DropTask dependantDrop = firtree.body.get(aminId).branch.get(number);

                dependantDrop.inData[to] = drop.outData[from];

                boolean ready = true;
                if (dependantDrop.numberOfDaughterProc == -2) {

                    for (int j = 0; j < dependantDrop.numberOfMainComponents; j++) {
                        if (dependantDrop.inData[j] == null) {
                            ready = false;
                            break;
                        }
                    }

                    if (ready) {
                        if (counter.vokzal[dependantDrop.aminId] == null) {
                            counter.vokzal[dependantDrop.aminId] = new ArrayList<DropTask>();
                        }
                        counter.vokzal[dependantDrop.aminId].add(dependantDrop);
                        counter.isEmptyVokzal = false;
                        dependantDrop.numberOfDaughterProc = -1;
                    } else {
                        ready = true;
                    }

                }

                if (dependantDrop.numberOfDaughterProc > -1) {
                    for (int j = dependantDrop.numberOfMainComponents; j < dependantDrop.inputDataLength; j++) {
                        if (dependantDrop.inData[j] == null) {
                            ready = false;
                            break;
                        }
                    }

                    if (ready && dependantDrop.numberOfDaughterProc != myRank) {
                        int length = dependantDrop.inputDataLength - dependantDrop.numberOfMainComponents;
                        Element[] additionalComponents = new Element[length];
                        System.arraycopy(dependantDrop.inData, dependantDrop.numberOfMainComponents, additionalComponents, 0, length);

                        Object[] tmpS = {additionalComponents, aminId, dropId, myRank};
                        Tools.sendObjects(tmpS, dependantDrop.numberOfDaughterProc, 3);
                        System.out.println("Sending additional components from  " + myRank + " to " + dependantDrop.numberOfDaughterProc);

                    }

                }
            } else {

                Amin amin = firtree.body.get(aminId);
                amin.resultForOutFunction[to] = drop.outData[from];

                for (int j = 0; j < amin.resultForOutFunction.length; j++) {
                    if (amin.resultForOutFunction[i] == null) {
                        isReadyOutputFunction = false;
                        break;
                    }
                }
            }
        }

        return isReadyOutputFunction;

    }
}
