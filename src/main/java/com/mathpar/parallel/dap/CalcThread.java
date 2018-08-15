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
    DropTask inputDrop;
    int flagOfMyState;
    int myRank;
    ArrayList<DropTask>[] vokzal;
    Ring ring;

    public CalcThread(Pine f, Ring ring) throws MPIException {
        thread = new Thread(this, "CalcThread");
        thread.setPriority(2);
        flToExit = false;
        isEmptyVokzal = true;
        firtree = f;
        this.ring = ring;
        flagOfMyState = 0;
        vokzal = new ArrayList[30];
        myRank = MPI.COMM_WORLD.getRank();
        thread.start();
    }

    public void DoneThread() {
        flToExit = true;
    }

    /**
     * The task has come. We write it to currentDrop
     *
     * @param dropInfo = {int type, int proc, int amin, int drop, Element[]
     * data}
     */
    void putDropInVokzal(Object[] dropInfo) {
         System.out.println("I'm in putDropInVokzal, recieved task, myrank is " + myRank);
        DropTask newDrop = Tools.getDropObject((int) dropInfo[0]);
        newDrop.aminId = (int) dropInfo[2];
        newDrop.dropId = (int) dropInfo[3];
        newDrop.procId = (int) dropInfo[1];
        Element[] mas = (Element[]) dropInfo[4];
        System.arraycopy(mas, 0, newDrop.inData, 0, mas.length);
        inputDrop = newDrop;
        
        if (vokzal[newDrop.aminId] == null) {
            vokzal[newDrop.aminId] = new ArrayList<DropTask>();
        }

        vokzal[newDrop.aminId].add(newDrop);
        newDrop.numberOfDaughterProc = -1;
        isEmptyVokzal = false;
    }

    /**
     *
     * @param drop
     *
     * @return true if ready outputFunctionData
     */
    public boolean writeResultsToAmin(DropTask drop) {

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
                        if (vokzal[dependantDrop.aminId] == null) {
                            vokzal[dependantDrop.aminId] = new ArrayList<DropTask>();
                        }
                        vokzal[dependantDrop.aminId].add(dependantDrop);
                        dependantDrop.numberOfDaughterProc = -1;
                    }
                }
            } else {

                Amin amin = firtree.body.get(aminId);
                amin.resultForOutFunction[to] = drop.outData[from];
                for (int j = 0; j < amin.resultForOutFunction.length; j++) {
                    //   System.out.println("amin.resultForOutFunction[j] =  " + amin.resultForOutFunction[j]);
                    if (amin.resultForOutFunction[j] == null) {
                        isReadyOutputFunction = false;
                        break;
                    }
                }

            }

        }

        // System.out.println(" isReadyOutputFunction" + isReadyOutputFunction);
        return isReadyOutputFunction;

    }

    @Override
    public void run() {

        while (!flToExit) {
            if (isEmptyVokzal) {
                continue;
            }
            try {

                getTask();
                ProcFunc();
                // System.out.println("isCalc " + isCalc + "isNotEmptyAvailibleList " + isNotEmptyAvailibleList);
            } catch (MPIException ex) {
                Logger.getLogger(CalcThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void writeResultsAfterInpFunc(DropTask drop, Amin curAmin, Element[] resInputFunc) {
         System.out.println("drop.arcs[0].length" + drop.arcs[0].length);
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
                System.out.println("dependantDrop.aminId" + dependantDrop.aminId);
                System.out.println("dependantDrop.dropId" + dependantDrop.dropId);
                if (vokzal[dependantDrop.aminId] == null) {
                    vokzal[dependantDrop.aminId] = new ArrayList<DropTask>();
                }
                vokzal[dependantDrop.aminId].add(dependantDrop);
                dependantDrop.numberOfDaughterProc = -1;
            }
        }

    }

    private void sendState() throws MPIException {
        int curState = firtree.CheckState() == 1 && Tools.isEmptyArray(vokzal) ? 1 : 0;

        firtree.SetState(curState);
        if (curState != flagOfMyState && inputDrop.procId != myRank) {
            flagOfMyState = curState;
            int[] state = {flagOfMyState, inputDrop.aminId, inputDrop.dropId};
            MPI.COMM_WORLD.send(state, state.length, MPI.INT, inputDrop.procId, 2);
            System.out.println("Sending state " + flagOfMyState + " from " + myRank + " to " + inputDrop.procId);
        }
    }

    public void writeAllResultsInFirtree(Amin amin, boolean isReady) {

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
    }

    private void sendWholeTask(Amin amin) throws MPIException {
        if (Tools.isEmptyArray(vokzal) && myRank == 0 && firtree.body.get(0).aminState == 2) {
            Object[] res = {amin.outputData, (Integer) amin.parentAmin, (Integer) amin.parentDrop, new Integer(-1)};
            System.out.println("&&&&&&&&&&Sending result of whole task " + myRank);
            for (int i = 0; i < amin.outputData.length; i++) {
                System.out.println("amin.outputData " + amin.outputData[i]);

            }

            Tools.sendObjects(res, myRank, 3);
            amin = null;

        }
    }

    private void getTask() {

        DropTask dropToTake = null;
        for (int i = 0; i < vokzal.length; i++) {
            if (vokzal[i] != null && !vokzal[i].isEmpty()) {
                dropToTake = vokzal[i].get(0);
                vokzal[i].remove(0);
                break;
            }
        }
        
        System.out.println("DROP = " + dropToTake.aminId+ dropToTake.dropId + dropToTake.type );
        currentDrop = dropToTake;

        dropToTake.numberOfDaughterProc = myRank;

    }

    private void ProcFunc() throws MPIException {
        //System.out.println("started proc func");

        boolean flagLittle = currentDrop.isItLeaf();
        Amin curAmin = null;

        // --------------------- start recursive -----------------------
        if (!flagLittle) {

            int firtreeSize = firtree.body.size();

            curAmin = new Amin(currentDrop.type, currentDrop.procId, currentDrop.aminId, currentDrop.dropId, firtreeSize);

            System.arraycopy(currentDrop.inData, 0, curAmin.inputData, 0, curAmin.inputData.length);

            //если пришла задача со всеми входними даними, записиваем неглавние компоненти в список для виходной функции
            if (currentDrop.numberOfMainComponents != curAmin.inputData.length) {

                Element[] notMainComponents = new Element[curAmin.inputData.length - currentDrop.numberOfMainComponents];
                System.arraycopy(curAmin.inputData, currentDrop.numberOfMainComponents, notMainComponents, 0, notMainComponents.length);

                for (int k = 0; k < notMainComponents.length; k++) {
                    curAmin.resultForOutFunction[currentDrop.connectionsOfNotMain[k]] = (curAmin.inputData)[currentDrop.numberOfMainComponents + k];
                }
            }

            firtree.body.add(curAmin);

            Element[] resInputFunc = curAmin.branch.get(0).inputFunction(curAmin.inputData);

            writeResultsAfterInpFunc(currentDrop, curAmin, resInputFunc);

        }

        Amin amin = null;
        // --------------------- start Little -----------------------
        if (flagLittle) {

            sendState();

            currentDrop.sequentialCalc(ring);

            if (currentDrop.procId == myRank) {// This task from me!

                boolean isReadyOutputData = writeResultsToAmin(currentDrop);

                firtree.body.get(currentDrop.aminId).branch.get(currentDrop.dropId).state = 2;

                amin = firtree.body.get(currentDrop.aminId);

                writeAllResultsInFirtree(amin, isReadyOutputData);

                if (amin.parentProc != myRank) {
                    Object[] res = {amin.outputData, amin.parentAmin, amin.parentDrop, -1};
                    Tools.sendObjects(res, amin.parentProc, 3);
                }

            } else {
                Object[] res = {currentDrop.outData, currentDrop.aminId, currentDrop.dropId, -1};
                Tools.sendObjects(res, currentDrop.procId, 3);
                System.out.println("@@@@@@@@@@Sending result from " + myRank + " to " + currentDrop.procId);

            }

        } // ------------------- end Little -----------------------

        sendWholeTask(amin);

        System.out.println("Tools.isEmptyArray(vokzal)  --- " + Tools.isEmptyArray(vokzal));
        isEmptyVokzal = Tools.isEmptyArray(vokzal);
        if (!isEmptyVokzal) {
            getTask();
        }

        return;

    }

}
