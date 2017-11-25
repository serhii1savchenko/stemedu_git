package com.mathpar.students.ukma17i41.liba;

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

public class Module2_4 {
      public static void sendObject(Object a, int proc, int tag) throws MPIException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
        System.out.println("send result");
    }

    public static Object recvObject(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {
        Status st = MPI.COMM_WORLD.probe(proc, tag);
        int size = st.getCount(MPI.BYTE);
        byte[] tmp = new byte[size];
        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE,
                proc, tag);
        Object res = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        res = ois.readObject();
        return res;
    }

    public static void sendArrayOfObjects(Object[] a, int proc, int tag) throws MPIException, IOException {
        for (int i = 0; i < a.length; i++) {
            sendObject(a[i], proc, tag + i);
        }
    }

    public static void sendObjects(Object[] a, int proc, int tag) throws MPIException {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            java.io.ObjectOutputStream oos
                    = new java.io.ObjectOutputStream(bos);
            for (int i = 0; i < a.length; i++) {
                oos.writeObject(a[i]);
            }
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

    public static void task1() throws MPIException, IOException, ClassNotFoundException{
        if(MPI.COMM_WORLD.getRank() == 1)
        {
            ExampleClass exampleObject = new ExampleClass(true);
            System.out.println(exampleObject.getExampleVariable());
            sendObject(exampleObject, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            ExampleClass recievedObject = (ExampleClass) recvObject(1, 1);
            System.out.println("Object successfully received");
            System.out.println(recievedObject.getExampleVariable());
        }
    }

    public static void task2(ExampleClass[] exampleObjectsArray) throws MPIException, IOException, ClassNotFoundException  {
        int myRank = MPI.COMM_WORLD.getRank();

        if (myRank == 1) {

            sendArrayOfObjects(exampleObjectsArray, 3, 1);
        }
        if (myRank == 3) {
            ExampleClass[] recievedExampleObjects = convertObjects(recvArrayOfObjects(1, 1));
            System.out.println("Objects successfully received");

            for (ExampleClass exampleObject:recievedExampleObjects) {
                System.out.println(exampleObject.getExampleVariable());
            }
        }
    }

    public static void task3(ExampleClass[] exampleObjectsArray) throws MPIException, IOException, ClassNotFoundException {
        if(MPI.COMM_WORLD.getRank() == 1)
        {
            sendObjects(exampleObjectsArray, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            ExampleClass[] recievedExampleObjects = convertObjects(recvObjects(4,1,1));
            System.out.println("Objects successfully received");

            for (ExampleClass exampleObject:recievedExampleObjects) {
                System.out.println(exampleObject.getExampleVariable());
            }
        }
    }

    public static void task4() throws MPIException, IOException, ClassNotFoundException{
        ExampleClass exampleObject = new ExampleClass(true);
        System.out.println(exampleObject.getExampleVariable());
        System.out.println(MPI.COMM_WORLD.getRank() + " processor sends");

        ExampleClass receivedExampleClass = (ExampleClass) bcastObject(exampleObject, MPI.COMM_WORLD.getRank());
        System.out.println(MPI.COMM_WORLD.getRank() +" got exampleObject");
        System.out.println(receivedExampleClass.getExampleVariable());
    }
    public static void task5(ExampleClass[] exampleObjectsArray) throws MPIException, IOException, ClassNotFoundException{
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

        /*
        /usr/bin/mpirun --default-hostfile none  -np 4 java -cp /home/lbodia/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/liba/Module2_4
        
        Output:
        
        true
        send result
        Object successfully received
        true

        */

        // task1();

 
        /*
        /usr/bin/mpirun --default-hostfile none  -np 4 java -cp /home/lbodia/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/liba/Module2_4
        
        send result
        send result
        send result
        send result
        send result
        Objects successfully received
        false
        true
        false
        true
        */
        
        // task2(exampleObjectsArray);
        
        
        /*
        /usr/bin/mpirun --default-hostfile none  -np 4 java -cp /home/lbodia/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/liba/Module2_4

        Objects successfully received
        false
        true
        false
        true

        */
        
        // task3(exampleObjectsArray);
        
        
        /*
        /usr/bin/mpirun --default-hostfile none  -np 4 java -cp /home/lbodia/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/liba/Module2_4
        
        true
        2 processor sends
        true
        3 processor sends
        true
        1 processor sends
        true
        0 processor sends
        3 got exampleObject
        true
        0 got exampleObject2 got exampleObject
        true

        true
        1 got exampleObject
        true

        
        */
        
        // task4();
        
        
        /*
        /usr/bin/mpirun --default-hostfile none  -np 4 java -cp /home/lbodia/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/liba/Module2_4
        
        Starting programm
        Success. Rank is 0
        Success. Rank is 3
        Success. Rank is 2
        Success. Rank is 1
        
        */
//        task5(exampleObjectsArray);

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
