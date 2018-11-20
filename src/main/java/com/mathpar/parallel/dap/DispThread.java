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
    static boolean flagOfMyDeparture = false;
    TreeSet<Integer> freeProcs;
    ArrayList<int[]>[] terminal;
    Map<Integer, ArrayList<Integer>> pNodes;

    static int myLevel = 20;
    int childsLevel = 20;
    int totalLevel = Math.min(myLevel, childsLevel);
    static int sentLevel = 20;

    // теги:
    //     0: сообщение содержит задачу
    //     1: сообщение содержит свободные узлы
    //     2: сообщение содержит состояние процесора
    //     3: сообщение содержит результат задачи
    //     5: сообщение содержит команду на завершение (вся задача посчитана)
    public DispThread(int startType, Pine f, long sTime, String[] args, Element[] data, Ring ring) throws MPIException {
        firtree = f;
        sleepTime = sTime;
        executeTime = 0;
        myRank = MPI.COMM_WORLD.getRank();
        freeProcs = new TreeSet<Integer>();
        terminal = new ArrayList[21];
        for (int i = 0; i < terminal.length; i++) {
            terminal[i] = new ArrayList<int[]>();
        }
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
        myLevel = 0;
        counter.putDropInVokzal(mas);

    }

    private void exit() {
        System.out.println("--------------------------------5");
        mode = -1;
        System.out.println("вся задача посчитана, myrank is   " + myRank);
        counter.DoneThread();
    }

    private void receiveTask(int cnt) throws MPIException {

        Object[] tmpAr = new Object[cnt];
        tmpAr = Tools.recvObjects(cnt, MPI.ANY_SOURCE, 0);
        System.out.println("Recieved task, myrank is   " + myRank);

        counter.putDropInVokzal(tmpAr);
        if (freeProcs.contains(myRank)) {
            freeProcs.remove(myRank);
        }
    }

    private void receiveFreeProcs(int cnt) throws MPIException {
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

    private void procLevel(int cnt, Integer daughter) throws MPIException {
        int[] tmr = new int[cnt];
        MPI.COMM_WORLD.recv(tmr, cnt, MPI.INT, daughter, 2);
        System.out.println("Recieved состояние процесора, myrank is   " + myRank);
        for (int i = 0; i < terminal[tmr[1]].size(); i++) {
            if (terminal[tmr[1]].get(i)[0] == daughter) {
                terminal[tmr[0]].add(terminal[tmr[1]].get(i));
                terminal[tmr[1]].remove(i);
                break;
            }
        }

        if (tmr[0] < childsLevel) {
            childsLevel = tmr[0];
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

            //задача посчитана
            if (myRank == nodes[0] && Tools.isEmptyArray(counter.vokzal) && firtree.body.get(0).aminState == 2) {
                endProgramme(cntProc, tmp, nodes);
            }

            if (amin.parentAmin != myRank) {
                Object[] res = {amin.outputData, (Integer) amin.parentAmin, (Integer) amin.parentDrop, sentLevel};
                Tools.sendObjects(res, amin.parentProc, 3);

                pNodes.get(amin.parentProc).remove((Integer) amin.aminIdInFirtree);

                if (pNodes.get(amin.parentProc).isEmpty()) {
                    pNodes.remove((Integer) amin.parentProc);
                }

                System.out.println("Sending result (disp) from  " + myRank + " to " + amin.parentProc);
            }
        }

    }

    private void addDaugter(int daughtProcs, int curDrop, int curAmin) {

        boolean newDaughter = true;
        for (int i = 0; i < terminal[20].size(); i++) {
            if (terminal[20].get(i)[0] == daughtProcs) {
                int[] newMas = new int[terminal[20].get(i).length + 2];
                System.arraycopy(terminal[20].get(i), 0, newMas, 0, terminal[20].get(i).length);
                newMas[newMas.length - 2] = curDrop;
                newMas[newMas.length - 1] = curAmin;
                terminal[20].remove(i);
                terminal[20].add(newMas);
                newDaughter = false;
                break;
            }
        }
        if (newDaughter) {
            int[] history = new int[] {daughtProcs, curAmin, curDrop};
            terminal[20].add(history);
        }

    }

    private void deleteDaughter(Integer daughter, Object[] tmp) {
        int level = (int) tmp[3];
        for (int i = 0; i < terminal[level].size(); i++) {
            if (terminal[level].get(i)[0] == daughter) {
                if (terminal[level].get(i).length == 3) {
                    terminal[level].remove(i);

                    if (childsLevel == level && terminal[level].isEmpty()) {
                        for (int l = childsLevel + 1; l < terminal.length; l++) {
                            if (!terminal[level].isEmpty()) {
                                childsLevel = l;
                                break;
                            }
                        }
                    }
                } else {
                    int index = -1;
                    for (int j = 1; j < terminal[level].get(i).length; j += 2) {
                        if (terminal[level].get(i)[j] == (int) tmp[1] && terminal[level].get(i)[j + 1] == (int) tmp[2]) {
                            index = j;
                            break;
                        }
                    }
                    int[] newMas = new int[terminal[level].get(i).length - 2];
                    System.arraycopy(terminal[level].get(i), 0, newMas, 0, index);
                    System.arraycopy(terminal[level].get(i), index + 2, newMas, index, terminal[level].get(i).length - (index + 2));
                    terminal[level].remove(i);
                    terminal[level].add(newMas);
                }
                break;
            } else {
                System.out.println("Cant find daughter " + daughter + "  for deleting from terminal!!!");
            }
        }
    }

    private void sendDrops(int number) throws MPIException {
        for (int i = 0; i < counter.vokzal[myLevel].size(); i++) {
            System.out.println("HEY in for " + myRank);
            if(freeProcs.size()==0) break;
            
            DropTask curTask = counter.vokzal[myLevel].get(i);
            int curAmin = curTask.aminId;
            int curDrop = curTask.dropId;

            Object[] tmpS = {curTask};

            if (number <= 1) {

                System.out.println("number< =  1");

                int daughter = (int) freeProcs.toArray()[i];
                System.out.println("Sending drop " + curTask + " from  " + myRank + " to " + daughter);
                Tools.sendObjects(tmpS, daughter, 0);

                curTask.numberOfDaughterProc = daughter;

                freeProcs.remove(daughter);
                addDaugter(daughter, curDrop, curAmin);
            } else {
                System.out.println("number>  1 = " + number);

                int[] daughtProcs = new int[number];
                for (int j = 0; j < daughtProcs.length; j++) {
                    daughtProcs[j] = (int) freeProcs.toArray()[j];

                    freeProcs.remove((int) freeProcs.toArray()[j]);
                    System.out.println("remove from free");

                    System.out.println("daughtProcs[j]" + daughtProcs[j]);
                }

                System.out.println("Sending drop plus procs" + curTask + " from  " + myRank + " to " + daughtProcs[0]);
                Tools.sendObjects(tmpS, daughtProcs[0], 0);

                int[] daughtFreeProcs = new int[daughtProcs.length - 1];
                System.arraycopy(daughtProcs, 1, daughtFreeProcs, 0, daughtFreeProcs.length);

                System.out.println("Sending freeProc from  " + myRank + " to " + daughtProcs[0]);
                MPI.COMM_WORLD.send(daughtFreeProcs, daughtFreeProcs.length, MPI.INT, daughtProcs[0], 1);

                curTask.numberOfDaughterProc = daughtProcs[0];
                addDaugter(daughtProcs[0], curDrop, curAmin);

            }
            counter.vokzal[myLevel].remove(i);
            System.out.println("vokzal[myLevel].size() after del = " + counter.vokzal[myLevel].size());
            counter.changeLevel();

        }
    }

    private void sendFreeToDaughter(int number) throws MPIException {

        if (freeProcs.size() > terminal[childsLevel].size()) {
            for (int j = 0; j < terminal[childsLevel].size(); j++) {
                int[] procsToSend = new int[(int) number];

                for (int i = 0; i < number; i++) {
                    procsToSend[i] = (int) freeProcs.toArray()[i];
                    freeProcs.remove(i);
                }
                MPI.COMM_WORLD.send(procsToSend, procsToSend.length, MPI.INT, terminal[childsLevel].get(j)[0], 1);
                System.out.println("Sending freeProc from  " + myRank + " to " + terminal[childsLevel].get(j)[0]);
            }
        } else {
            for (int i = 0; i < freeProcs.size(); i++) {
                int[] tmpS = {(int) freeProcs.toArray()[i]};
                MPI.COMM_WORLD.send(tmpS, tmpS.length, MPI.INT, terminal[childsLevel].get(i)[0], 1);
                System.out.println("Sending freeProc from  " + myRank + " to " + terminal[childsLevel].get(i)[0]);
            }
        }

    }

    private void sendDropsAndFreeProcs() throws MPIException {

        System.out.println("Send drops and free procs");
        System.out.println("myLevel = " + myLevel);
        System.out.println("childsLevel = " + childsLevel);

        double number = 0;
        System.out.println("vokzal[myLevel].size() = " + counter.vokzal[myLevel].size());
        if (myLevel == childsLevel && childsLevel != 20) {
            System.out.println("myLevel == childsLevel && childsLevel != 20");
            number = freeProcs.size() / (counter.vokzal[myLevel].size() + terminal[childsLevel].size());
            sendDrops((int) number);
            sendFreeToDaughter((int) number);

        } else if (myLevel < childsLevel && counter.vokzal[myLevel].size() != 0&& myLevel!=0) {
            System.out.println("myLevel < childsLevel");
            number = freeProcs.size() / counter.vokzal[myLevel].size();
            sendDrops((int) number);
        } else if (myLevel > childsLevel && terminal[childsLevel].size() != 0) {
            System.out.println("myLevel > childsLevel");
            number = freeProcs.size() / terminal[childsLevel].size();
            sendFreeToDaughter((int) number);

        } else if (myLevel == childsLevel && childsLevel == 20) {
            System.out.println("myLevel == childsLevel && childsLevel == 20");
            int parent = (int) pNodes.keySet().toArray()[0];

            MPI.COMM_WORLD.send(freeProcs, freeProcs.size(), MPI.INT, parent, 1);
            System.out.println("Sending freeProcs from  " + myRank + " to " + parent);

        }

    }

    private void doMeFree() {
        if (counter.isEmptyVokzal && !flagOfMyDeparture) {
            freeProcs.add(myRank);
            flagOfMyDeparture = true;
        }
    }

    private void sendLevel() throws MPIException {

        if (counter.isEmptyVokzal && childsLevel == 20) {
            myLevel = 20;
        }
        if (totalLevel != sentLevel) {
            sentLevel = totalLevel;

            int[] state = {totalLevel, sentLevel};

            for (Object o : pNodes.keySet()) {
                MPI.COMM_WORLD.send(state, state.length, MPI.INT, (int) o, 2);
                System.out.println("Sending state " + totalLevel + " from " + myRank + " to " + (int) o);
            }
        }

    }

    public void execute(int startType, String[] args, Element[] data, int[] nodes, Ring ring) throws mpi.MPIException, InterruptedException, IOException, ClassNotFoundException {

        //System.out.println("In execute, myrank is   " + myRank);
        int cntProc = nodes.length;

        Thread disp = Thread.currentThread();
        disp.setPriority(8);
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
            // System.out.println("**In DispThreadThread cycle, myRank = " + myRank);

            Status info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, MPI.ANY_TAG);

            if (info != null) {
                int tag = info.getTag();
                int cnt = info.getCount(MPI.INT);
                Integer daughter;
                System.out.println("MYRANK = " + myRank);

                if (tag == 5) {
                    exit();
                }
                if (tag == 0) {
                    System.out.println("Get task ");
                    //режим ожидания задачи
                    cnt = info.getCount(MPI.INT);
                    receiveTask(cnt);
                    flagOfMyDeparture = false;
                }
                if (tag == 1) {
                    System.out.println("Get free procs");
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
                        System.out.println("Get proc state");
                        cnt = info.getCount(MPI.INT);
                        daughter = info.getSource();

                        procLevel(cnt, daughter);

                        info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 2);
                    }
                }
                if (tag == 3) {

                    //результат 
                    while (info != null) {
                        System.out.println("Get result");
                        daughter = info.getSource();

                        receiveResult(daughter, cntProc, nodes, ring);

                        info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 3);
                    }
                }

            }

            sendLevel();

            doMeFree();
            //Отправка дропов и свободних процесоров 
            if (freeProcs.size() != 0) {
                sendDropsAndFreeProcs();
            }

            //System.out.println("GO TO SLEEP myRank = " + myRank);
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
