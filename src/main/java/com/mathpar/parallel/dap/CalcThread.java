/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templatesamin.outputData
 * and open the template in the editor.
 */
//package com.mathpar.students.ukma17i41.bosa.parallel.engine;
package com.mathpar.parallel.dap;

import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.MPI;
import mpi.MPIException;

/**
 * @author alla
 * @param
 */
public class CalcThread implements Runnable {

    private Pine firtree;
    Thread thread;
    volatile boolean flToExit;
    volatile boolean isEmptyVokzal;
    DropTask currentDrop;
    int myRank;
    ArrayList<DropTask>[] vokzal;
    Ring ring;
    Map<Integer, ArrayList<Integer>> pNodes;

    public CalcThread(Pine f, Map<Integer, ArrayList<Integer>> pN, Ring ring) throws MPIException {
        thread = new Thread(this, "CalcThread");
        thread.setPriority(2);
        flToExit = false;
        isEmptyVokzal = true;
        firtree = f;
        pNodes = pN;
        this.ring = ring;
        vokzal = new ArrayList[20];
        for (int i = 0; i < vokzal.length; i++) {
            vokzal[i] = new ArrayList<DropTask>();
        }
        myRank = MPI.COMM_WORLD.getRank();
        thread.start();
    }

    public void DoneThread() {
        flToExit = true;
    }

    private void addToVokzal(DropTask drop) {
        vokzal[drop.recNum].add(drop);
        drop.numberOfDaughterProc = -1;
        isEmptyVokzal = false;
    }

    public void putDropInVokzal(Object[] dropInfo) {
        System.out.println("I'm in putDropInVokzal, recieved task, myrank is " + myRank);

        DropTask drop = (DropTask) dropInfo[0];
        if (drop.recNum < DispThread.myLevel) {
            DispThread.myLevel = drop.recNum;
        }
        addToVokzal(drop);
    }

    public boolean writeResultsToAmin(DropTask drop) throws MPIException {

        int aminId = drop.aminId;
        int dropId = drop.dropId;
        boolean isReadyOutputFunction = true;
        for (int i = 0; i < drop.arcs[dropId + 1].length; i += 3) {

            int number = drop.arcs[dropId + 1][i];
            int from = drop.arcs[dropId + 1][i + 1];
            int to = drop.arcs[dropId + 1][i + 2];

            if (drop.arcs[number].length != 0) {
                isReadyOutputFunction = false;

                DropTask dependantDrop = firtree.body.get(aminId).branch.get(number - 1);

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
                        addToVokzal(dependantDrop);
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

                    if (ready) {
                        addNotMainComponents(dependantDrop, aminId, dropId);
                    }
                }
            } else {

                Amin amin = firtree.body.get(aminId);
                amin.resultForOutFunction[to] = drop.outData[from];
                for (int j = 0; j < amin.resultForOutFunction.length; j++) {
                    //System.out.println("amin.resultForOutFunction[j] =  " + amin.resultForOutFunction[j]);
                    if (amin.resultForOutFunction[j] == null) {
                        isReadyOutputFunction = false;
                        break;
                    }
                }
            }
        }

        return isReadyOutputFunction;

    }

    private void addNotMainComponents(DropTask dependantDrop, int aminId, int dropId) throws MPIException {

        int length = dependantDrop.inputDataLength - dependantDrop.numberOfMainComponents;
        Element[] additionalComponents = new Element[length];
        System.arraycopy(dependantDrop.inData, dependantDrop.numberOfMainComponents, additionalComponents, 0, length);

        if (dependantDrop.numberOfDaughterProc != myRank) {
            Object[] tmpS = {additionalComponents, aminId, dropId};
            Tools.sendObjects(tmpS, dependantDrop.numberOfDaughterProc, 3);
            System.out.println("Sending additional components from  " + myRank + " to " + dependantDrop.numberOfDaughterProc);
        } else {
            for (int j = 0; j < firtree.body.size(); j++) {
                if (firtree.body.get(j).parentProc == dependantDrop.procId && firtree.body.get(j).parentAmin == dependantDrop.aminId
                        && firtree.body.get(j).parentDrop == dependantDrop.dropId) {
                    int[] mas = Tools.getDropObject(firtree.body.get(j).type).connectionsOfNotMain;
                    for (int k = 0; k < mas.length; k++) {
                        firtree.body.get(j).resultForOutFunction[mas[k]] = additionalComponents[k];

                    }
                }
            }
        }
    }

    private void writeResultsAfterInpFunc(DropTask drop, Amin curAmin, Element[] resInputFunc) {

        for (int i = 0; i < drop.arcs[0].length; i += 3) {
            int numOfDependantDrop = drop.arcs[0][i];
            int from = drop.arcs[0][i + 1];
            int to = drop.arcs[0][i + 2];

            DropTask dependantDrop = curAmin.branch.get(numOfDependantDrop - 1);

            dependantDrop.inData[to] = resInputFunc[from];

            boolean ready = true;

            for (int j = 0; j < dependantDrop.numberOfMainComponents; j++) {
                if (dependantDrop.inData[j] == null) {
                    ready = false;
                    break;
                }
            }

            if (ready) {

                addToVokzal(dependantDrop);
            }
        }

    }

    public Amin writeAllResultsInFirtree(Amin amin, boolean isReady) throws MPIException {

        while (isReady) {

            amin.outputData = Tools.getDropObject(amin.type).outputFunction(amin.resultForOutFunction, ring);
            firtree.body.get(amin.parentAmin).branch.get(amin.parentDrop).outData = amin.outputData;

            amin.aminState = 2;
            if (amin.parentProc == myRank) {

                isReady = writeResultsToAmin(firtree.body.get(amin.parentAmin).branch.get(amin.parentDrop));

                amin = firtree.body.get(amin.parentAmin);

            } else {
                isReady = false;
            }
        }
        return amin;
    }

    private void sendWholeTask(Amin amin) throws MPIException {

        Object[] res = {amin.outputData, (Integer) amin.parentAmin, (Integer) amin.parentDrop};
        System.out.println("&&&&&&&&&&Sending result of whole task " + myRank);

        Tools.sendObjects(res, myRank, 3);
        amin = null;

    }

    public void changeLevel() {

        if (vokzal[DispThread.myLevel].size()==0) {
            for (int k = DispThread.myLevel; k < vokzal.length; k++) {
                if (!vokzal[k].isEmpty()) {
                    DispThread.myLevel = k;
                    break;
                }
            }
        }
    }

    private void getTask() {

        changeLevel();
        
        currentDrop = vokzal[DispThread.myLevel].get(0);

        currentDrop.numberOfDaughterProc = myRank;

        vokzal[DispThread.myLevel].remove(0);
       // changeLevel();

    }

    private void deleteParent(Integer procNum, Integer num) {

        pNodes.get(procNum).remove(num);

        if (pNodes.get(procNum).isEmpty()) {
            pNodes.remove(procNum);
        }
    }

    private void addParent(Integer procNum, Integer num) {
        if (!pNodes.keySet().contains(procNum)) {
            ArrayList<Integer> am = new ArrayList<Integer>();
            am.add(num);
            pNodes.put(procNum, am);
        } else {
            pNodes.get(procNum).add(num);
        }
    }

    @Override
    public void run() {

        while (!flToExit) {
           // System.out.println("///In CalcThread cycle, myRank = " + myRank);
            if (isEmptyVokzal) {
                continue;
            }
            try {
                getTask();
                ProcFunc();

            } catch (MPIException ex) {
                Logger.getLogger(CalcThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void ProcFunc() throws MPIException {

        boolean flagLittle = currentDrop.isItLeaf();
        Amin curAmin = null;

        // --------------------- start recursive -----------------------
        if (!flagLittle) {

            int firtreeSize = firtree.body.size();

            curAmin = new Amin(currentDrop.type, currentDrop.procId, currentDrop.aminId, currentDrop.dropId, firtreeSize, myRank, currentDrop.recNum + 1);

            System.arraycopy(currentDrop.inData, 0, curAmin.inputData, 0, curAmin.inputData.length);

            boolean exist = true;
            if (curAmin.inputData.length != currentDrop.numberOfMainComponents) {
                for (int j = currentDrop.numberOfMainComponents; j < curAmin.inputData.length; j++) {
                    if (curAmin.inputData[j] == null) {
                        exist = false;
                        break;
                    }
                }
                //если пришла задача со всеми входними даними, записиваем неглавние компоненти в список для виходной функции
                if (exist) {
                    Element[] notMainComponents = new Element[curAmin.inputData.length - currentDrop.numberOfMainComponents];
                    System.arraycopy(curAmin.inputData, currentDrop.numberOfMainComponents, notMainComponents, 0, notMainComponents.length);

                    for (int k = 0; k < notMainComponents.length; k++) {
                        curAmin.resultForOutFunction[currentDrop.connectionsOfNotMain[k]] = notMainComponents[k];
                    }
                }
            }
            firtree.body.add(curAmin);

            // System.out.println("myRank = " + myRank + "curAmin.parentAmin = " + curAmin.parentAmin);
            if (curAmin.parentProc != myRank) {
                addParent(curAmin.parentProc, curAmin.aminIdInFirtree);
            }

            Element[] resInputFunc = curAmin.branch.get(0).inputFunction(curAmin.inputData);

            writeResultsAfterInpFunc(currentDrop, curAmin, resInputFunc);

        }

        Amin amin = null;
        // --------------------- start Little -----------------------
        if (flagLittle) {

            currentDrop.state = 1;

            if (currentDrop.procId != myRank) {
                addParent(currentDrop.procId, -1);
            }

            currentDrop.sequentialCalc(ring);

            if (currentDrop.procId == myRank) {// This task from me!

                boolean isReadyOutputData = writeResultsToAmin(currentDrop);

                firtree.body.get(currentDrop.aminId).branch.get(currentDrop.dropId).state = 2;

                amin = firtree.body.get(currentDrop.aminId);

                amin = writeAllResultsInFirtree(amin, isReadyOutputData);

                if (amin.parentProc != myRank) {
                    Object[] res = {amin.outputData, amin.parentAmin, amin.parentDrop, DispThread.sentLevel};
                    System.out.println("Sending  from " + myRank + " to " + currentDrop.procId);
                    Tools.sendObjects(res, amin.parentProc, 3);

                    deleteParent(amin.parentProc, amin.aminIdInFirtree);
                }

            } else {
                Object[] res = {currentDrop.outData, currentDrop.aminId, currentDrop.dropId, DispThread.sentLevel};
                System.out.println("@@@@@@@@@@Sending result from " + myRank + " to " + currentDrop.procId);
                Tools.sendObjects(res, currentDrop.procId, 3);

                deleteParent(currentDrop.procId, -1);
            }

        } // ------------------- end Little -----------------------

        if (myRank == 0 && Tools.isEmptyArray(vokzal) && firtree.body.get(0).aminState == 2) {
            sendWholeTask(amin);
        }

        isEmptyVokzal = Tools.isEmptyArray(vokzal);

        if (isEmptyVokzal) {
            System.out.println("Go to WAIT  VOKZAL IS EMPTY");
        }

        return;

    }

}
