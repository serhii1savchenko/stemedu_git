/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.kravchenko;

import com.mathpar.students.llp2.student.helloworldmpi.Transport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

/**
 *
 * @author kravchenko
 */
class MyClass implements Serializable {
        boolean bln = true;
        
        MyClass(boolean _bln) {
            bln = _bln;
        }
        
        public boolean getbln() {
            return bln;
        }
    }
public class Task2_4 {
    
    public static void sendObject(Object a,int proc, int tag)
            throws MPIException, IOException {
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
        System.out.println("Object send");
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
        return res;
}
    /*
    To run: 
    openmpi/bin/mpirun --default-hostfile none  -np 4 java -cp /home/kravchenko/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task2_4
    */
    
    public static void First_Task()
            throws MPIException, IOException, ClassNotFoundException{
        
        if(MPI.COMM_WORLD.getRank() == 1)
        {
            MyClass exampleObject = new MyClass(true);
            System.out.println(exampleObject.getbln());
            sendObject(exampleObject, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            MyClass recievedObject = (MyClass) recvObject(1, 1);
            System.out.println("Object receive");
            System.out.println(recievedObject.getbln());
        }
    }
    
    /*output:
    true
    Object send
    Object receive
    true
    */
    
    public static void sendArrayOfObjects(Object[] a, int proc, int tag)
            throws MPIException,IOException {
        
        for (int i = 0; i < a.length; i++)
            sendObject(a[i], proc, tag + i);
    }
    
    public static void sendObjects(Object[] a, int proc, int tag)
            throws MPIException {
        
        ByteArrayOutputStream bos = null;
        try {bos = new ByteArrayOutputStream();
        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
        
        for (int i = 0; i < a.length; i++)
            oos.writeObject(a[i]);
        bos.toByteArray();
        } catch (Exception ex) {
            Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] temp = bos.toByteArray();
        ByteBuffer buf = MPI.newByteBuffer(temp.length);
        buf.put(temp);
        MPI.COMM_WORLD.iSend(buf, temp.length,MPI.BYTE, proc, tag);
    }
    
    public static Object[] recvArrayOfObjects(int proc,int tag)
            throws MPIException, IOException,ClassNotFoundException {
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
        try {ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        ObjectInputStream ois = new ObjectInputStream(bis);
        
        for (int i = 0; i < arr.length; i++)
            res[i] = (Object) ois.readObject();
        } catch (Exception ex) {
            Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    /*
    To run: 
    openmpi/bin/mpirun --default-hostfile none  -np 4 java -cp /home/kravchenko/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task2_4
    */
    
    public static void Second_Task(MyClass[] exampleObjectsArray)
            throws MPIException, IOException, ClassNotFoundException{
        
        int myRank = MPI.COMM_WORLD.getRank();
        
        if (myRank == 1) {
            sendArrayOfObjects(exampleObjectsArray, 3, 1);
        }
        if (myRank == 3) {
            MyClass[] recievedExampleObjects = convertObjects(recvArrayOfObjects(1, 1));
            System.out.println("Object receive");

            for (MyClass exampleObject:recievedExampleObjects) {
                System.out.println(exampleObject.getbln());
            }
        }
    }
    
    /*output:
    Object send
    Object send
    Object send
    Object send
    Object send
    Object receive
    false
    false
    false
    true

    */
    
     /*
    To run: 
    openmpi/bin/mpirun --default-hostfile none  -np 4 java -cp /home/kravchenko/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task2_4
    */
    
    public static void Third_Task(MyClass[] myArray)
            throws MPIException, IOException, ClassNotFoundException{
        
        if(MPI.COMM_WORLD.getRank() == 1)
        {
            sendObjects(myArray, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            MyClass[] recievedExampleObjects = convertObjects(recvObjects(4,1,1));
            System.out.println("Objects successfully received");

            for (MyClass exampleObject:recievedExampleObjects) {
                System.out.println(exampleObject.getbln());
            }
        }
    }
    
    /*output:
    Objects successfully received
    false
    false
    false
    true
    */
    
    public static Object bcastObject(Object o, int root)
            throws IOException,MPIException,ClassNotFoundException{
        byte []tmp=null;
        int []size=new int[1];
        int rank=MPI.COMM_WORLD.getRank();
        
        if (rank==root){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            java.io.ObjectOutputStream oos =new java.io.ObjectOutputStream(bos);
            oos.writeObject(o);
            tmp=bos.toByteArray();
            size[0]=tmp.length;
        }
        
        MPI.COMM_WORLD.bcast(size,1,MPI.INT,root);
        if (rank!=root) tmp=new byte[size[0]];
        MPI.COMM_WORLD.bcast(tmp,tmp.length,MPI.BYTE,root);
        
        if (rank!=root){
            ByteArrayInputStream bis =new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        }
        return o;
    }
    
    public static void bcastObjectArray(Object []o,int count, int root)
            throws IOException, MPIException,ClassNotFoundException{

        byte []tmp=null;
        int []size=new int[1];
        int rank=MPI.COMM_WORLD.getRank();
        
        if (rank==root){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            java.io.ObjectOutputStream oos =new java.io.ObjectOutputStream(bos);
            
            for (int i=0; i<count; i++)
                oos.writeObject(o[i]);
            tmp=bos.toByteArray();
            size[0]=tmp.length;
        }
        
        MPI.COMM_WORLD.bcast(size,1,MPI.INT,root);
        if (rank!=root) tmp=new byte[size[0]];
        MPI.COMM_WORLD.bcast(tmp,tmp.length,MPI.BYTE,root);
        
        if (rank!=root){
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            
            for (int i=0; i<count; i++)
                o[i]=ois.readObject();
        }
}
    
    /*
    To run: 
    openmpi/bin/mpirun --default-hostfile none  -np 5 java -cp /home/kravchenko/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task2_4
    */
    
    public static void Fourth_Task() 
            throws MPIException, IOException, ClassNotFoundException{

        MyClass bln = new MyClass(true);
        System.out.println(bln.getbln());
        System.out.println(MPI.COMM_WORLD.getRank() + " Processor Send");

        MyClass receivedExampleClass = (MyClass) bcastObject(bln, MPI.COMM_WORLD.getRank());
        System.out.println(MPI.COMM_WORLD.getRank() +" Processor Reiceve");
        System.out.println(receivedExampleClass.getbln());
    }
    
    /*output:
    truetrue
    5 Processor Send
    
    0 Processor Send
    true
    2 Processor Send
    true
    1 Processor Send
    true    
    3 Processor Send
    true
    4 Processor Send
    0 Processor Reiceve
    true
    2 Processor Reiceve
    true
    5 Processor Reiceve
    true
    3 Processor Reiceve
    true
    1 Processor Reiceve
    true
    4 Processor Reiceve
    true

    */
    
     /*
    To run: 
    openmpi/bin/mpirun --default-hostfile none  -np 5 java -cp /home/kravchenko/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task2_4
    */
    public static void Fifth_Task(MyClass[] myArray)
            throws MPIException, IOException, ClassNotFoundException{
        
        System.out.println("Begin");
        bcastObjectArray(myArray, myArray.length, MPI.COMM_WORLD.getRank());

        System.out.println("Finnish. Rank is " + MPI.COMM_WORLD.getRank());
    }
    
    /*output:
    Begin
    Begin
    Begin
    Begin
    Begin
    Begin
    Finnish. Rank is 3
    Finnish. Rank is 4
    Finnish. Rank is 1
    Finnish. Rank is 2
    Finnish. Rank is 5
    Finnish. Rank is 0

    */
   
    
    private static MyClass[] convertObjects(Object[] objects){
        MyClass[] arrClass = new MyClass[objects.length];

        for (int i=0; i<objects.length; ++i){
            arrClass[i] = (MyClass) objects[i];
        }

        return arrClass;
    }
    
public static void main(String[] args)
        throws MPIException, IOException, ClassNotFoundException {
    
    MPI.Init(args);
 
        MyClass x1 = new MyClass(false);
        MyClass x2 = new MyClass(false);
        MyClass x3 = new MyClass(false);
        MyClass x4 = new MyClass(true);
        MyClass x5 = new MyClass(false);
        MyClass[] myArray = {x1, x2, x3, x4, x5};
    
    //First_Task();
    //Second_Task(myArray);
    //Third_Task(myArray);
    //Fourth_Task();
    Fifth_Task(myArray);
    
    MPI.Finalize();
}

}

