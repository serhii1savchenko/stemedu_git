/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Zadorozhnyi.Module3;

import java.io.IOException;
import mpi.MPIException;
import mpi.*;

import static com.mathpar.parallel.utils.MPITransport.sendObject;

public class sendArrayOfObjects {
    public static void main(Object[] a,int proc, int tag) throws MPIException,IOException {
// îòïðàâêà îáúåêòà a[i] ïðîöåññîðó ñ íîìåðîì proc
// ñ òåãîì tag + i
    for (int i = 0; i < a.length; i++)
        sendObject(a[i], proc, tag + i);
    }
}

//mpirun java sendArrayOfObjects