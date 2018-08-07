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

    /**
     *
     * @param startType
     * @param args
     * @param data
     * @param nodes
     *
     * @throws mpi.MPIException
     * @throws InterruptedException
     */
    private void rootWork(TreeSet<Integer> freeProcs, int[] nodes, int startType, Element[] data) {
        System.out.println("i'm root");
        for (int i = 1; i < nodes.length; i++) {
            freeProcs.add(nodes[i]);
        }
        Object[] mas = new Object[] {startType, 0, 0, 0, data};
        counter.setAmin(mas);

    }

    private void exit() {
        System.out.println("--------------------------------5");
        mode = -1;
        System.out.println("вся задача посчитана, myrank is   " + myRank);
        counter.DoneThread();
    }

    private void receiveTask(TreeSet<Integer> freeProcs) throws MPIException {
        System.out.println("--------------------------------0");
        System.out.println("режим ожидания задачи--STATUS=== ");
        Object[] tmpAr = new Object[5];
        //Parent=taskInfo.getSource();
        tmpAr = Tools.recvObjects(5, MPI.ANY_SOURCE, 0);
        System.out.println("Recieved task, myrank is   " + myRank);
        if (freeProcs.contains(myRank)) {
            freeProcs.remove(myRank);
        }
        counter.setAmin(tmpAr);
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
    }

    private void procState(Map<Integer, int[]> dNodes, int cnt, Integer daughter) throws MPIException {
        int[] tmr = new int[cnt];
        MPI.COMM_WORLD.recv(tmr, cnt, MPI.INT, daughter, 2);
        System.out.println("Recieved состояние процесора, myrank is   " + myRank);
        dNodes.get(daughter)[0] = tmr[0];

        firtree.body.get(tmr[1]).branch.get(tmr[2]).state = tmr[0];

        if (counter.taskLevels[0].isEmpty() && firtree.CheckState() == 1
                && !counter.GetFlag_IsItCalcState()) {
            firtree.SetState(1);
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
        } else {
            if ((int) tmp[3] != -1) {

                {
                    Avariya:
                    for (int j = 0; j < firtree.body.size(); j++) {
                        if (firtree.body.get(j).parentProc == (int) tmp[3] && firtree.body.get(j).parentAmin == (int) tmp[1]
                                && firtree.body.get(j).dropId == (int) tmp[2]) {
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
            } else {
                System.out.println("Integer) tmp[3] != myRank   ");
                DropTask currentDrop = firtree.body.get((int) tmp[1]).branch.get((int) tmp[2]);

                currentDrop.outData = (Element[]) tmp[0];
                currentDrop.state = 2;

                if ((int) tmp[3] == myRank) {
                    for (int i = 0; i < currentDrop.outData.length; i++) {
                        System.out.println("Result:   " + currentDrop.outData[i]);
                    }
                }

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

                isReadyF = writeResultsToAmin(currentDrop, (int) tmp[1], (int) tmp[2]);
                amin = firtree.body.get((int) tmp[1]);
            }
            while (isReadyF) {
                amin.outputData = Tools.getDropObject(amin.type).outputFunction(amin.resultForOutFunction, ring);

                if (amin.parentProc == myRank) {
                    isReadyF = writeResultsToAmin(firtree.body.get(amin.parentAmin).branch.get(amin.dropId), amin.parentAmin, amin.dropId);
                    amin = firtree.body.get(amin.parentAmin);
                } else {
                    isReadyF = false;
                }
            }

            //задача посчитана - перейти в режим 2
            if (amin.parentAmin == -1) {
                //задача посчитана в корневом узле - завершение работы
                int[] tmpAr = {24};
                for (int j = 1; j < cntProc; j++) {
                    MPI.COMM_WORLD.send(tmpAr, 1, MPI.INT, nodes[j], 5);
                }
                mode = -1;
                counter.DoneThread();
            } else {
                Object[] res = {amin.outputData, (Integer) amin.parentAmin, (Integer) amin.dropId};
                Tools.sendObjects(res, amin.parentProc, 3);
                System.out.println("Sending result (disp) from  " + myRank + " to " + amin.parentProc);
            }
        }

    }

    private void sendDropsAndFreeProcsToDaughers(TreeSet<Integer> freeProcs, Map<Integer, int[]> dNodes) throws MPIException {

        System.out.println("--------------------------------111111");
        ArrayList<Integer> free = new ArrayList(freeProcs);
        if (!counter.GetFlag_IsItCalcState()) {

            free.add(myRank);
        }
        int number = freeProcs.size() / counter.taskLevels[0].size();
        if (number <= 1) {
            for (int i = 0; i < counter.taskLevels[0].size(); i++) {
                //counter.taskLevels;
                DropTask curTask = counter.taskLevels[0].get(0);
                int curAmin = curTask.aminFirtree;
                int curDrop = firtree.body.get(curAmin).branch.indexOf(curTask);
                Object[] tmpS = {curTask.GetType(), myRank, curAmin, curDrop, curTask.inData};
                Tools.sendObjects(tmpS, free.get(i), 0);
                System.out.println("Sending task from  " + myRank + " to " + free.get(i));
                curTask.numberOfDaughterProc = free.get(i);

                counter.taskLevels[0].remove(0);
                if (counter.taskLevels[0].isEmpty()) {
                    DropTask[] newMasOfLevels = new DropTask[counter.taskLevels.length - 1];
                    System.arraycopy(counter.taskLevels, 1, newMasOfLevels, 0, newMasOfLevels.length);
                }
            }
        } else {
            int[] daughtProcs = new int[number];
            for (int j = 0; j < counter.taskLevels[0].size(); j++) {
                if (freeProcs.size() != 0) {
                    for (int i = 0; i < number; i++) {
                        daughtProcs[i] = (int) freeProcs.toArray()[i];
                        freeProcs.remove(i);
                    }

                    DropTask curTask = counter.taskLevels[0].get(0);
                    int curAmin = curTask.aminFirtree;
                    int curDrop = firtree.body.get(curAmin).branch.indexOf(curTask);
                    Object[] tmpS = {curTask.GetType(), myRank, curAmin, curDrop, curTask.inData};

                    if (!dNodes.keySet().contains(daughtProcs[0])) {
                        int[] history = new int[] {0, curAmin, curDrop};
                        dNodes.put(daughtProcs[0], history);
                    } else {
                        int[] newMas = new int[dNodes.get(daughtProcs[0]).length + 2];
                        System.arraycopy(dNodes.get(daughtProcs[0]), 0, newMas, 0, dNodes.get(daughtProcs[0]).length);
                        newMas[newMas.length - 1] = curDrop;
                        newMas[newMas.length - 1] = curAmin;
                        dNodes.put(daughtProcs[0], newMas);
                    }

                    Tools.sendObjects(tmpS, daughtProcs[0], 0);
                    System.out.println("Sending task from  " + myRank + " to " + daughtProcs[0]);
                    MPI.COMM_WORLD.send(daughtProcs, 1, MPI.INT, daughtProcs[0], 1);
                    System.out.println("Sending freeProcs from  " + myRank + " to " + daughtProcs[0]);
                    curTask.numberOfDaughterProc = daughtProcs[0];

                    counter.taskLevels[0].remove(0);
                    if (counter.taskLevels[0].isEmpty()) {
                        DropTask[] newMasOfLevels = new DropTask[counter.taskLevels.length - 1];
                        System.arraycopy(counter.taskLevels, 1, newMasOfLevels, 0, newMasOfLevels.length);
                    }

                }
            }
        }

    }

    private void sendFreeProcsIFinishedNoTask(TreeSet<Integer> freeProcs, Map<Integer, int[]> dNodes) throws MPIException {
        System.out.println("--------------------------------2222");
        if (!counter.GetFlag_IsItCalcState() && dNodes.size() > 0) {//?
            freeProcs.add(myRank);
        }

        ArrayList<Integer> daughterInNeed = new ArrayList();

        for (Object o : dNodes.keySet()) {
            if (dNodes.get(o).equals(0)) {
                daughterInNeed.add((Integer) o);
            }
        }

        if (freeProcs.size() > daughterInNeed.size()) {
            int procNum = freeProcs.size() / daughterInNeed.size();
            int[] procsToSend = new int[procNum];
            for (int j = 0; j < daughterInNeed.size(); j++) {
                for (int i = 0; i < procNum; i++) {
                    procsToSend[i] = (int) freeProcs.toArray()[i];
                    freeProcs.remove(i);
                }
                MPI.COMM_WORLD.send(procsToSend, 1, MPI.INT, daughterInNeed.get(j), 1);
                System.out.println("Sending freeProc from  " + myRank + " to " + daughterInNeed.get(j));
            }
        } else {
            for (int i = 0; i < freeProcs.size(); i++) {
                int[] tmpS = {(int) freeProcs.toArray()[i]};
                MPI.COMM_WORLD.send(tmpS, 1, MPI.INT, daughterInNeed.get(i), 1);
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
            }
        }
        MPI.COMM_WORLD.send(freeProcs, 1, MPI.INT, parent, 1);
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

            // Status exitInfo = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 5);
            if (info != null) {
                int tag = info.getTag();
                System.out.println("^^^^^^^^^^^INFO = " + info);
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
                    while (info != null) {
                        System.out.println("--------------------------------2");
                        cnt = info.getCount(MPI.INT);
                        daughter = info.getSource();

                        procState(dNodes, cnt, daughter);

                        info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 2);
                    }
                }
                if (tag == 3) {

                    while (info != null) {
                        System.out.println("--------------------------------3");
                        Integer daughter = info.getSource();
                        receiveResult(freeProcs, dNodes, daughter, cntProc, nodes, ring);
                        info = MPI.COMM_WORLD.iProbe(MPI.ANY_SOURCE, 3);
                    }
                }
            }

            //Отправка дропов і свободних процесоров, если есть задания
            if (freeProcs.size() > 0 && counter.taskLevels[counter.sizeOfTaskLevel - 1].size() > 0) {
                sendDropsAndFreeProcsToDaughers(freeProcs, dNodes);
            }

            //Отправка свободних процесоров дочерним, если я заканачил и нет больше заданий
            if (freeProcs.size() > 0 && counter.taskLevels[0].isEmpty()) {
                sendFreeProcsIFinishedNoTask(freeProcs, dNodes);
            }

            // немає дочірніх і я закінчив немає завдань
            if (!counter.GetFlag_IsItCalcState() && dNodes.size() != 0 && counter.taskLevels[0].isEmpty()) {
                sendFreeProcsToParent(freeProcs);

            }

            //периодичность диспетчера
            disp.sleep(sleepTime);
            //System.out.println("#After sleep! myRank " + myRank);

        }

        counter.thread.join();
        executeTime = System.currentTimeMillis() - executeTime;
        if (myRank == rootNumb) {
            System.out.println("DDP done.");
            System.out.println("executeTime = " + executeTime);
        }

    }

    public boolean writeResultsToAmin(DropTask drop, int aminId, int dropId) throws MPIException {

        boolean isReadyOutputFunction = true;
        for (int i = 0; i < drop.arcs[dropId + 1].length; i += 3) {
            int number = drop.arcs[dropId + 1][i];

            if (drop.arcs[number] != null) {
                isReadyOutputFunction = false;
                DropTask dependantDrop = firtree.body.get(aminId).branch.get(number);
                int from = drop.arcs[number][i + 1];
                int to = drop.arcs[number][i + 2];

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
                        counter.taskLevels[0].add(dependantDrop);
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
                int from = drop.arcs[number][i + 1];
                int to = drop.arcs[number][i + 2];
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
