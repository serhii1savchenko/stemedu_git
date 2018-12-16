/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Dymchenko.Module3;

import com.mathpar.students.llp2.student.helloworldmpi.Transport;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.*;

public class recvObjects {
    public static Object[] recvObjects(int m, int proc, int tag)throws MPIException {
        Status s = MPI.COMM_WORLD.probe(proc, tag);
        int n = s.getCount(MPI.BYTE);
        byte[] arr = new byte[n];
        MPI.COMM_WORLD.recv(arr, n, MPI.BYTE, proc, tag);
        Object[] res = new Object[m];
        try {ByteArrayInputStream bis =
        new ByteArrayInputStream(arr);
        ObjectInputStream ois =
        new ObjectInputStream(bis);
        for (int i = 0; i < arr.length; i++)  res[i] = (Object) ois.readObject();
        } catch (Exception ex) {
        Logger.getLogger(Transport.class.getName()).
        log(Level.SEVERE, null, ex);}
        return res;
    }
}