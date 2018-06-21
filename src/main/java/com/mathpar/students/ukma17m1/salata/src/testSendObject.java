/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.salata.src;

import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import java.io.*;

/**
 *
 * @author leximiro
 */
public class testSendObject {
	//mpirun -np 2 java -cp /home/leximiro/Desktop/stemedu/target/classes com/mathpar/students/ukma17m1/salata/src/testSendObject param1 param2    
    
    public static void main(String[] args)throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        String str = "some string";
        sendObject(str,1,1);
        System.out.println(recvObject(1,1));
        MPI.Finalize();
        
    }
    
    public static void sendObject(Object a,int proc, int tag)throws MPIException, IOException {
        
        ByteArrayOutputStream bos =
        new ByteArrayOutputStream();
        ObjectOutputStream oos =
        new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE,
        proc, tag);
    }
    
    public static Object recvObject(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {
        Status st = MPI.COMM_WORLD.probe(proc, tag);
        int size = st.getCount(MPI.BYTE);
        byte[] tmp = new byte[size];
        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE,
        proc, tag);
        Object res = null;
        ByteArrayInputStream bis =
        new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        res = ois.readObject();
        return res;
       }
        
}
