package com.mathpar.students.ukma.Gorshkova.modul3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;


public class Task4_1_RecvObject {  
    
    public static Object recvObject(int proc, int tag)
    throws MPIException, IOException, ClassNotFoundException {
        
        Status st = MPI.COMM_WORLD.probe(proc, tag);

        int size = st.getCount(MPI.BYTE);

        byte[] tmp = new byte[size];

        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE, proc, tag);
        Object res = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        res = ois.readObject();
        System.out.println(res);
        return res;
    }
    
    public static void sendObject(Object a, int proc, int tag)
    throws MPIException, IOException {
        ByteArrayOutputStream bos =
            new ByteArrayOutputStream();
        ObjectOutputStream oos =
            new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
   }
}
/*
cd 
mpijavac 
*/