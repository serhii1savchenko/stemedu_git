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
    volatile boolean isCalc;
    volatile boolean isNotEmptyAvailibleList;
    Object[] currentDrop;
    boolean isReadyOutputData;
    Object[] inputDrop;
    int flagOfMyState;
    int myRank;
    ArrayList<DropTask>[] taskLevels;
    int sizeOfTaskLevel;
    Ring ring;

    public boolean GetFlag_IsItCalcState() {
        return isCalc;
    }

    public CalcThread(Pine f, Ring ring) throws MPIException {
        thread = new Thread(this, "CalcThread");
        thread.setPriority(2);
        flToExit = false;
        isCalc = false;
        isNotEmptyAvailibleList = false;
        firtree = f;
        isReadyOutputData = false;
        this.ring = ring; 
        flagOfMyState = 0;
        sizeOfTaskLevel = 0;
        taskLevels = new ArrayList[20];
        myRank = MPI.COMM_WORLD.getRank();
        thread.start();
    }

    public void DoneThread() {
        flToExit = true;
    }
    /**
     * The task has come. We write it to currentDrop
     * @param dropInfo = {int type, int proc, int amin, int drop,  Element[] data}
     */
    void setAmin(Object[] dropInfo) {
       // System.out.println("I'm in setAmin, recieved task, myrank is " + myRank);
        currentDrop = dropInfo;
        inputDrop = dropInfo;
        isCalc = true;
    }


    /**
     *
     * @param drop
     *
     * @return true if ready outputFunctionData
     */
    public boolean writeResultsToAmin(DropTask drop, int aminId, int dropId) {

        boolean isReadyOutputFunction = true;
        for (int i = 0; i < drop.arcs[dropId + 1].length; i += 3) {
            int number = drop.arcs[dropId + 1][i];
            if (drop.arcs[number].length != 0) {
                isReadyOutputFunction = false;

                DropTask dependantDrop = firtree.body.get(aminId).branch.get(number - 1);
                int from = drop.arcs[dropId + 1][i + 1];
                int to = drop.arcs[dropId + 1][i + 2];
               

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
                        taskLevels[sizeOfTaskLevel - 1].add(dependantDrop);
                        dependantDrop.numberOfDaughterProc = myRank;
                    }
                }
            } else {

                int from = drop.arcs[dropId + 1][i + 1];
                int to = drop.arcs[dropId + 1][i + 2];
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
            if (!isCalc && !isNotEmptyAvailibleList) {
                continue;
            }
            try {

                ProcFunc();
               // System.out.println("isCalc " + isCalc + "isNotEmptyAvailibleList " + isNotEmptyAvailibleList);
            } catch (MPIException ex) {
                Logger.getLogger(CalcThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void ProcFunc() throws MPIException {
        //System.out.println("started proc func");

        DropTask drop = Tools.getDropObject((int) currentDrop[0]);

        Element[] mas = (Element[]) currentDrop[4];
        System.arraycopy(mas, 0, drop.inData, 0, mas.length);

        boolean flagLittle = drop.isItLeaf();
        Amin curAmin = null;
        boolean flagOfFirstEnter = false;
        if (!flagLittle) {
            int firtreeSize = firtree.body.size();

            curAmin = new Amin((int) currentDrop[0], (int) currentDrop[1], (int) currentDrop[2], (int) currentDrop[3], firtreeSize);

            // System.out.println("Task isn't little, amin created, myrank is " + myRank);
            Element[] masInput = (Element[]) currentDrop[4];
            System.arraycopy(masInput, 0, curAmin.inputData, 0, curAmin.inputData.length);

         

            if (drop.numberOfDaughterProc != curAmin.inputData.length) {
                Element[] notMainComponents = new Element[curAmin.inputData.length - drop.numberOfMainComponents];
                System.arraycopy(curAmin.inputData, drop.numberOfMainComponents, notMainComponents, 0, notMainComponents.length);
                for (int k = 0; k < notMainComponents.length; k++) {
                    curAmin.resultForOutFunction[drop.connectionsOfNotMain[k]] = (curAmin.inputData)[drop.numberOfMainComponents + k];
                }
            }

            firtree.body.add(curAmin);

            Element[] resInputFunc = curAmin.branch.get(0).inputFunction(curAmin.inputData);
            //System.out.println("inputFunction done, task type" + currentDrop[0] + "myrank is " + myRank);
            //передача даних из resInputFunc в дропи амина 
            //создание масива доступних дропов

            if (taskLevels[sizeOfTaskLevel] == null) {
                taskLevels[sizeOfTaskLevel] = new ArrayList<DropTask>();
                sizeOfTaskLevel++;
                flagOfFirstEnter = true;
            }
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
                    taskLevels[sizeOfTaskLevel - 1].add(dependantDrop);
                    dependantDrop.level = sizeOfTaskLevel;

                    dependantDrop.numberOfDaughterProc = myRank;
                }
            }

        }
        int indexOfTaskLevel = sizeOfTaskLevel == 0 ? 0 : sizeOfTaskLevel - 1;
        boolean jumpToEnd = false;
        
        if (flagLittle) {// --------------------- start Little -----------------------

            int curState = 0;
            if (taskLevels[indexOfTaskLevel] == null) {
                if (firtree.CheckState() == 1) {
                    curState = 1;
                }
            } else {
                curState = firtree.CheckState() == 1 && taskLevels[indexOfTaskLevel].isEmpty() ? 1 : 0;
            }
            firtree.SetState(curState);
            if (curState != flagOfMyState&&(int) inputDrop[1]!=myRank) {
                flagOfMyState = curState;
                int[] state = {flagOfMyState, (int) inputDrop[2], (int) inputDrop[3]};
                MPI.COMM_WORLD.send(state, 1, MPI.INT, (int) inputDrop[1], 2);
                System.out.println("Sending state " + flagOfMyState + " from " + myRank + " to " + inputDrop[1]);
            }

            drop.sequentialCalc(ring);
            
           // System.out.println("seq calc done " + myRank);
            
             if (firtree.body.isEmpty()) {
                 
             Object[] res = {drop.outData, (Integer)inputDrop[2],  (Integer)inputDrop[3], new Integer(-1)};
             isCalc = false;
             Tools.sendObjects(res, myRank, 3);
             System.out.println("Sending result of whole task " + myRank);

             return;
           
             }

            if ((int) currentDrop[1] == myRank) {// This task from me!

                isReadyOutputData = writeResultsToAmin(drop, (int) currentDrop[2], (int) currentDrop[3]);

                firtree.body.get((int) currentDrop[2]).branch.get((int) currentDrop[3]).state = 2;

            } else {
                Object[] res = {drop.outData, (int) currentDrop[2], (int) currentDrop[3], -1};
                Tools.sendObjects(res, (int) currentDrop[1], 3);
                System.out.println("@@@@@@@@@@Sending result from " + myRank + " to " + currentDrop[1]);

                jumpToEnd = true;
            }

        } // ------------------- end Little -----------------------

        Amin amin = null;
        if (!jumpToEnd) { //------------------- end not jump to end -----------------------

            amin = firtree.body.get((int) currentDrop[2]);

            while (isReadyOutputData) {

                amin.outputData = Tools.getDropObject(amin.type).outputFunction(amin.resultForOutFunction, ring);
                firtree.body.get(amin.parentAmin).branch.get(amin.dropId).outData = amin.outputData;
                amin.aminState = 2;
                if (amin.parentProc == myRank) {
                   
                    isReadyOutputData = writeResultsToAmin(firtree.body.get(amin.parentAmin).branch.get(amin.dropId), amin.parentAmin, amin.dropId);
                    amin = firtree.body.get(amin.parentAmin);
                } else {
                    isReadyOutputData = false;
                }
            }

            if (amin.parentProc != myRank) {
                Object[] res = {amin.outputData, amin.parentAmin, amin.dropId, -1};
                 System.out.println("#######Sending result from " + myRank + " to " + amin.parentProc);
                Tools.sendObjects(res, amin.parentProc, 3);
                System.out.println("#######Sending result from " + myRank + " to " + amin.parentProc);
            }

        } //------------------- end not jump to end -----------------------
        
        
        for (int l = 1; l < sizeOfTaskLevel; l++) {
           
            if(taskLevels[l].isEmpty())
            {
            ArrayList<DropTask>[] newMas = new ArrayList[taskLevels.length];
            System.arraycopy(taskLevels, 0, newMas, 0, l);
            System.arraycopy(taskLevels, l + 1, newMas, l,  sizeOfTaskLevel - (l + 1));
            taskLevels = newMas;
            sizeOfTaskLevel--;
            indexOfTaskLevel = sizeOfTaskLevel == 0 ? 0 : sizeOfTaskLevel - 1;
            }
        }

        if (taskLevels[indexOfTaskLevel].isEmpty() && indexOfTaskLevel != 0) {
            taskLevels[indexOfTaskLevel] = null;
            sizeOfTaskLevel--;
            indexOfTaskLevel = sizeOfTaskLevel == 0 ? 0 : sizeOfTaskLevel - 1;
        }
        //System.out.println("!!!sizeOfTaskLevel " + sizeOfTaskLevel);
        

      /*  for (int l = 0; l < sizeOfTaskLevel; l++) {
             System.out.println("askLevels[l].size()" + taskLevels[l].size());
            for (int k = 0; k < taskLevels[l].size(); k++) {
               
                System.out.println("!!!availible " + l + "   --- " + taskLevels[l].get(k).type);
            }
        }*/

        if (taskLevels[0].isEmpty() && sizeOfTaskLevel > 1) {
            ArrayList<DropTask>[] newMasOfLevels = new ArrayList[taskLevels.length];
            System.arraycopy(taskLevels, 1, newMasOfLevels, 0, sizeOfTaskLevel);
            taskLevels = newMasOfLevels;
            sizeOfTaskLevel--;
            indexOfTaskLevel = sizeOfTaskLevel == 0 ? 0 : sizeOfTaskLevel - 1;
        }
        if (taskLevels[0].isEmpty() && myRank == 0 && firtree.body.get(0).aminState == 2) {
            Object[] res = {amin.outputData, (Integer) amin.parentAmin, (Integer) amin.dropId, new Integer(-1)};
            System.out.println("&&&&&&&&&&Sending result of whole task " + myRank);
            for(int i = 0; i<amin.outputData.length; i++)
            {
                System.out.println("amin.outputData " + amin.outputData[i]);
            
            }
            
            System.out.println("amin.parentAmin " + amin.parentAmin);
            System.out.println("amin.dropId " + amin.dropId);
            Tools.sendObjects(res, myRank, 3);
            System.out.println("&&&&&&&&&&Sending result of whole task " + myRank);
            amin = null;

        }

       // System.out.println("!!@#indexOfTaskLevel " + indexOfTaskLevel);

        

        isNotEmptyAvailibleList = !taskLevels[indexOfTaskLevel].isEmpty();
        if (isNotEmptyAvailibleList) {

           // System.out.println("!!isNotEmptyAvailibleList");
            int numAmin = taskLevels[indexOfTaskLevel].get(0).aminFirtree;

            int numDrop = firtree.body.get(numAmin).branch.indexOf(taskLevels[indexOfTaskLevel].get(0));

            Object[] tmp = {taskLevels[indexOfTaskLevel].get(0).type, myRank, numAmin, numDrop, taskLevels[indexOfTaskLevel].get(0).inData};

            currentDrop = tmp;
            inputDrop = tmp;
            taskLevels[indexOfTaskLevel].get(0).numberOfDaughterProc = myRank;

            taskLevels[indexOfTaskLevel].remove(0);

            //System.out.println("sizeOfTaskLevel " + sizeOfTaskLevel);
            isCalc = true;
        } else {
            isCalc = false;
        }

        return;

    }

}
