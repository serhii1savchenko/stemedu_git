/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.nedilska;

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
 * @author nedilska
 */

 //Run:  openmpi/bin/mpirun --hostfile hostfile -np 8 java -cp /home/nedilska/stemedu/target/classes com/mathpar/students/ukma17i41/nedilska/Module2_Chapter4 

public class Module2_Chapter4 {
	
	//4.1.Отправка и прием обьектов
        public static void sendObject(Object a, int proc, int tag) throws MPIException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
        System.out.println("Send ");
    }

    public static Object recvObject(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {
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
    
	//4.2.Отправка массива обьектов
    public static void sendArrayOfObjects(Object[] a, int proc, int tag) throws MPIException, IOException {
        for (int i = 0; i < a.length; i++) {
            sendObject(a[i], proc, tag + i);
        }
    }

    public static void sendObjects(Object[] a, int proc, int tag) throws MPIException {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
            for (int i = 0; i < a.length; i++) {
                oos.writeObject(a[i]);
            }
            bos.toByteArray();
        } catch (Exception ex) {
            Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] temp = bos.toByteArray();
        ByteBuffer buf = MPI.newByteBuffer(temp.length);
        buf.put(temp);
        MPI.COMM_WORLD.iSend(buf, temp.length, MPI.BYTE, proc, tag);
    }
    
	//4.3.Прием массива обьектов
    public static Object[] recvArrayOfObjects(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {
        Object[] o = new Object[4];
        for (int i = 0; i < 4; i++) {
            o[i] = recvObject(proc, tag + i);
        }
        return o;
    }

    public static Object[] recvObjects(int m, int proc, int tag) throws MPIException
    {
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
        }
        catch (Exception ex) {
            Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }
      
	//4.4.Коллективная рассылка одного обьекта или массива обьектов
    public static Object bcastObject(Object o, int root) throws IOException, MPIException, ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == root) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
            oos.writeObject(o);
            tmp = bos.toByteArray();
            size[0] = tmp.length;
        }
        MPI.COMM_WORLD.bcast(size, 1, MPI.INT, root);
        if (rank != root) {
            tmp = new byte[size[0]];
        }
        MPI.COMM_WORLD.bcast(tmp, tmp.length, MPI.BYTE, root);
        if (rank != root) {
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        }
        return o;
    }

    public static void bcastObjectArray(Object[] o, int count, int root) throws IOException, MPIException, ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == root) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
            for (int i = 0; i < count; i++) {
                oos.writeObject(o[i]);
            }
            tmp = bos.toByteArray();
            size[0] = tmp.length;
        }
        MPI.COMM_WORLD.bcast(size, 1, MPI.INT, root);
        if (rank != root) {
            tmp = new byte[size[0]];
        }
        MPI.COMM_WORLD.bcast(tmp, tmp.length, MPI.BYTE, root);
        if (rank != root) {
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            for (int i = 0; i < count; i++) {
                o[i] = ois.readObject();
            }
        }
    }

    private static ExampleClass[] convertObjects(Object[] objects){
        ExampleClass[] exampleClasses = new ExampleClass[objects.length];
        for (int i=0; i<objects.length; ++i){
            exampleClasses[i] = (ExampleClass) objects[i];
        }
        return exampleClasses;
    }

    public static void task4_1() throws MPIException, IOException, ClassNotFoundException{
        if(MPI.COMM_WORLD.getRank() == 1) {
            ExampleClass exampleObject = new ExampleClass(true);
            System.out.println(exampleObject.getExampleVariable());
            sendObject(exampleObject, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3) {
            ExampleClass recievedObject = (ExampleClass) recvObject(1, 1);
            System.out.println("Object received");
            System.out.println(recievedObject.getExampleVariable());
        }
    }

    public static void task4_2(ExampleClass[] exampleObjectsArray) throws MPIException, IOException, ClassNotFoundException  {
        int myRank = MPI.COMM_WORLD.getRank();
        if (myRank == 1) {
            sendArrayOfObjects(exampleObjectsArray, 3, 1);
        }
        if (myRank == 3) {
            ExampleClass[] recievedExampleObjects = convertObjects(recvArrayOfObjects(1, 1));
            System.out.println("Objects received");
            for (ExampleClass exampleObject:recievedExampleObjects) {
                System.out.println(exampleObject.getExampleVariable());
            }
        }
    }

    public static void task4_3(ExampleClass[] exampleObjectsArray) throws MPIException, IOException, ClassNotFoundException {
        if(MPI.COMM_WORLD.getRank() == 1) {
            sendObjects(exampleObjectsArray, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3) {
            ExampleClass[] recievedExampleObjects = convertObjects(recvObjects(4,1,1));
            System.out.println("Objects received");
            for (ExampleClass exampleObject:recievedExampleObjects) {
                System.out.println(exampleObject.getExampleVariable());
            }
        }
    }

    public static void task4_4() throws MPIException, IOException, ClassNotFoundException{
        ExampleClass exampleObject = new ExampleClass(true);
        System.out.println(exampleObject.getExampleVariable());
        System.out.println(MPI.COMM_WORLD.getRank() + " processor sends");

        ExampleClass receivedExampleClass = (ExampleClass) bcastObject(exampleObject, MPI.COMM_WORLD.getRank());
        System.out.println(MPI.COMM_WORLD.getRank() +" got exampleObject");
        System.out.println(receivedExampleClass.getExampleVariable());
    }
    
    public static void task4_example(ExampleClass[] exampleObjectsArray) throws MPIException, IOException, ClassNotFoundException{
        System.out.println("Starting programm");
        bcastObjectArray(exampleObjectsArray, exampleObjectsArray.length, MPI.COMM_WORLD.getRank());
        System.out.println("Success. Rank is " + MPI.COMM_WORLD.getRank());
    }
    
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        
        MPI.Init(args);
 
        ExampleClass var1 = new ExampleClass(false);
        ExampleClass var2 = new ExampleClass(true);
        ExampleClass var3 = new ExampleClass(false);
        ExampleClass var4 = new ExampleClass(true);
        ExampleClass var5 = new ExampleClass(true);
        ExampleClass[] exampleObjectsArray = {var1,var2,var3,var4, var5};
     
        // task4_1();
	
        /**
        * Output:
        * true
        * Send
        * Object received
        * true
        */

        // task4_2(exampleObjectsArray);
		
        /**
         * Output:
         * Send 
         * Send 
         * Send 
         * Send 
         * Send 
         * Objects received
         * false
         * true
         * false
         * true
         */

        // task4_3(exampleObjectsArray);
		
        /**
         * Output:
         * Nov 26, 2017 12:30:43 AM com.mathpar.students.ukma17i41.nedilska.Module2_Chapter4 recvObjects
         * SEVERE: null
         * java.lang.ArrayIndexOutOfBoundsException: 4
         * at com.mathpar.students.ukma17i41.nedilska.Module2_Chapter4.recvObjects(Module2_Chapter4.java:97)
         * at com.mathpar.students.ukma17i41.nedilska.Module2_Chapter4.task4_3(Module2_Chapter4.java:198)
         * at com.mathpar.students.ukma17i41.nedilska.Module2_Chapter4.main(Module2_Chapter4.java:259)
         * 
         * Objects received
         * false
         * true
         * false
         * true
         */

        // task4_4();
		
        /**
         * Output:
         * true
         * 5 processor sends
         * true
         * 6 processor sendstrue
         * 
         * true0 processor sends
         * 
         * true
         * 1 processor sends
         * 7 processor sends
         * true
         * 2 processor sends
         * true
         * 4 processor sends
         * 0 got exampleObject
         * true
         * 2 got exampleObject
         * true
         * 5 got exampleObject
         * true
         * true
         * 1 got exampleObject
         * true
         * 4 got exampleObject
         * true
         * 3 processor sends
         * 7 got exampleObject
         * true
         * 6 got exampleObject
         * true
         * 3 got exampleObject
         * true
         */
       
        //task4_example(exampleObjectsArray);
		
        /**
         * Output:
         * Starting programm
         * Starting programm
         * Starting programm
         * Starting programm
         * Starting programm
         * Success. Rank is 3
         * Success. Rank is 0
         * Success. Rank is 2
         * Success. Rank is 1
         * Success. Rank is 7
         * Starting programm
         * Starting programm
         * Starting programm
         * Success. Rank is 6
         * Success. Rank is 4
         * Success. Rank is 5
         */

        MPI.Finalize();

    }
}

class ExampleClass implements Serializable {
    boolean exampleVariable = true;

    ExampleClass(boolean _exampleVariable) {
        exampleVariable = _exampleVariable;
    }

    public boolean getExampleVariable() {
        return exampleVariable;
    }

}
