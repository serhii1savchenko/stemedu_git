/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.mathpar.students.ukma17i41.bosa.parallel.engine;
package com.mathpar.parallel.dap;

import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import java.io.Serializable;
import java.util.ArrayList;

import static com.mathpar.number.Ring.MatrixS;


public abstract class DropTask implements Serializable {

    Element[] inData;
    Element[] outData;
    int numberOfMainComponents;
    /**
     * 0-идет счет,
     * 1-идет листовой счет,
     * 2-закончен счет,
     */
    int state;
    int[][] arcs;
    ArrayList<DropTask> amin;
    int type;
    int resultForOutFunctionLength;
    int inputDataLength;
    int numberOfDaughterProc = -2;
    int [] connectionsOfNotMain;
    int aminId = -1;
    int dropId = -1;
    int procId = -1;
    int recNum = 0;
    
    public abstract ArrayList<DropTask> doAmin();
    public abstract void sequentialCalc(Ring ring);
    public abstract Element[] inputFunction(Element []input);
    public abstract Element[] outputFunction(Element[] input, Ring ring);
    public abstract boolean isItLeaf();
    public int GetType(){
        return type;
    }
   
    
     @Override
        public String toString() {
           String str = "";
           for (Element i: inData) {
                 str +=i + " ";
           }
           
           return str;
        }
}
