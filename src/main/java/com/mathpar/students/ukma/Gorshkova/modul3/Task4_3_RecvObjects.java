package com.mathpar.students.ukma.Gorshkova.modul3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import static com.mathpar.parallel.utils.MPITransport.recvObject;

public class Task4_3_RecvObjects {
    
    public static Object[] recvArrayOfObjects(int proc, int tag)
    throws MPIException, IOException, ClassNotFoundException {
        Object[] o = new Object[4];
        for (int i = 0; i < 4; i++)
            o[i] = recvObject(proc, tag + i);
        return o;
    }
    
    public static Object[] recvObjects(int m, int proc, int tag)
    throws MPIException {
        Status s = MPI.COMM_WORLD.probe(proc, tag);
        int n = s.getCount(MPI.BYTE);
        byte[] arr = new byte[n];
        MPI.COMM_WORLD.recv(arr, n, MPI.BYTE, proc, tag);
        Object[] res = new Object[m];
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(arr);
            ObjectInputStream ois = new ObjectInputStream(bis);
            for (int i = 0; i < arr.length; i++)
                res[i] = (Object) ois.readObject();
        } catch (Exception ex) {
            Logger.getLogger(MPI4_3_RecvObjects.class.getName()).
            log(Level.SEVERE, null, ex);
        }
        return res;
   }
}

/*
cd 
mpijavac -cp 
*/