/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.mathpar.students.ukma17i41.bosa.parallel.engine;
package com.mathpar.parallel.dap;

import com.mathpar.number.Element;
import java.util.ArrayList;

public class Amin {
    int parentProc;
    int parentAmin;
    int parentDrop;
    int type;
    int aminIdInFirtree;
    /**
     * 0-нет пока входных данных, 1-идет счет,2-идет листовой счет, 3-закончено,
     * 4-результаты все отправлены
     */
    int aminState;
    int recNumb;
    ArrayList<DropTask> branch;

    Element[] inputData;
    Element[] outputData;
    Element[] resultForOutFunction;

    public Amin(int type, int pProc, int pAmin, int dropNum, int index, int myRank, int recNum) {

        DropTask drop = Tools.getDropObject(type);

        this.type = type;
        branch = drop.doAmin();
        setIndexToDrops(index, myRank, recNum);
        inputData = new Element[drop.inputDataLength];
        resultForOutFunction = new Element[drop.resultForOutFunctionLength];
        outputData = null;
        parentProc = pProc;
        parentAmin = pAmin;
        parentDrop = dropNum;
        aminState = 0;
        recNumb = recNum;
        aminIdInFirtree = index;

    }

    private void setIndexToDrops(int index, int myRank, int recNum) {
        for (int i = 0; i < branch.size(); i++) {
            branch.get(i).aminId = index;
            branch.get(i).dropId = i;
            branch.get(i).procId = myRank;
            branch.get(i).recNum = recNum;
            
        }
    }

    @Override
    public String toString() {
        String str = "";
        for (DropTask i : branch) {
            str += i + "\n";
        }

        return str;
    }

    public int GetMyDrop(DropTask dp) {
        return branch.indexOf(dp);
    }

    public int SetState() {
        for (int i = 0; i < branch.size(); i++) {
            if (branch.get(i).state != 1 && branch.get(i).state != 2) {
                aminState = 0;
                return aminState;
            }
        }
        aminState = 1;
        return aminState;
    }

}
