package com.mathpar.students.ukma.Bohachek.Module3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

public class MPI4_1_RecvObject {
    
    public static void main(String[] args) 
    throws MPIException, InterruptedException, IOException, ClassNotFoundException {
        MPI.Init(args);
        int proc = Integer.parseInt(args[0]);
        int tag =Integer.parseInt(args[1]);
        
        sendObject(recvObject(proc,tag), proc, tag);
        
    }
        
    
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
mpirun --hostfile /home/elizabeth/hostfile -np 2 java -cp /home/elizabeth/stemedu/target/classes com.mathpar.students.ukma.Bohachek.Module3.MPI4_1_RecvObject 3 1

*/