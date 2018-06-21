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
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mathpar.students.llp2.student.helloworldmpi.Transport;
/**
 *
 * @author leximiro
 */
public class TestSendArrayOfObjects {
    
	//mpirun -np 2 java -cp /home/leximiro/Desktop/stemedu/target/classes com/mathpar/students/ukma17m1/salata/src/TestSendArrayOfObjects param1 param2    
   /*post:
    Input was param1 param2 after receiving this array we have output:
    param1
    param2
    */
    public static void main(String[] args) throws MPIException{
        MPI.Init(args);
        sendObjects(args,1,1);
        Object[] arr = recvObjects(args.length, 1, 1);
        for(Object obj : arr){
            System.out.println(obj);
        }
        MPI.Finalize();
    }
    
    
    
    
    
    
    
    
    
   public static void sendObjects(Object[] a, int proc, int tag) throws MPIException {
        ByteArrayOutputStream bos = null;
        try {bos = new ByteArrayOutputStream();
        ObjectOutputStream oos =
        new ObjectOutputStream(bos);
        for (int i = 0; i < a.length; i++)
        oos.writeObject(a[i]);
        bos.toByteArray();
        } catch (Exception ex) {
        Logger.getLogger(Transport.class.getName()).
        log(Level.SEVERE, null, ex);
        }
        byte[] temp = bos.toByteArray();
        ByteBuffer buf = MPI.newByteBuffer(temp.length);
        buf.put(temp);
        MPI.COMM_WORLD.iSend(buf, temp.length,
        MPI.BYTE, proc, tag);
       }
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
        for (int i = 0; i < arr.length; i++)
        res[i] = (Object) ois.readObject();
        } catch (Exception ex) {
        Logger.getLogger(Transport.class.getName()).
        log(Level.SEVERE, null, ex);}
        return res;
}   
}
