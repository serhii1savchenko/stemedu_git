/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Dymchenko.Module3;

import java.io.IOException;
import mpi.*;

import static com.mathpar.parallel.utils.MPITransport.recvObject;

public class recvArrayOfObjects {
    public static Object[] recvArrayOfObjects(int proc,int tag)throws MPIException, IOException,ClassNotFoundException
    {
        Object[] o = new Object[4];
        for (int i = 0; i < 4; i++)  o[i] = recvObject(proc, tag + i);
        return o;
    }
}