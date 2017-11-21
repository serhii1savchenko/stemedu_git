/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.sukharskyi;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.*;
import com.mathpar.parallel.utils.MPITransport;
import com.mathpar.students.llp2.student.helloworldmpi.Transport;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

/**
 *
 * @author probook
 */
public class SecondModuleTask {
    // run command:
    //   openmpi/bin/mpirun --hostfile hostfile  -np 4 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/SecondModuleTask 2
    // output:
    //    true
    //    Object successfully sent
    //    Object successfully received
    //    true
    private static void sendAndReceiveObject(String args[]) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        if(MPI.COMM_WORLD.getRank() == 1)
        {
            Dog dog = new Dog(true);
            System.out.println(dog.DoesEatCats());
            sendObject(dog, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            Dog recievedDog = (Dog) recvObject(1, 1);
            System.out.println("Object successfully received");
            System.out.println(recievedDog.DoesEatCats());
        }

        MPI.Finalize();
    }

    public static void sendObject(Object a, int proc, int tag)throws MPIException, IOException{
        ByteArrayOutputStream bos =new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE,proc, tag);
        System.out.println("Object successfully sent");
    }

    public static Object recvObject(int proc, int tag) throws MPIException, IOException,ClassNotFoundException {
        Status st = MPI.COMM_WORLD.probe(proc, tag);
        int size = st.getCount(MPI.BYTE);
        byte[] tmp = new byte[size];
        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE,proc, tag);
        Object res = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        res = ois.readObject();
        return res;
    }

    // run command:
    //   openmpi/bin/mpirun --hostfile hostfile  -np 4 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/SecondModuleTask 2
    // output:
    //    Object successfully sent
    //    Object successfully sent
    //    Object successfully sent
    //    Object successfully sent
    //    Objects successfully received
    //    true
    //    true
    //    false
    //    true
    private static void sendAndReceiveArrayOfObjects(String args[]) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        int myRank=MPI.COMM_WORLD.getRank();

        if(myRank == 1)
        {
            Dog[] dogs = getDogsArray();

            sendArrayOfObjects(dogs, 3, 1);
        }
        if(myRank == 3)
        {
            Dog[] recievedDogs = convertObjectsToDogs(recvArrayOfObjects(1,1));
            System.out.println("Objects successfully received");

            showDogsProperties(recievedDogs);
        }

        MPI.Finalize();
    }

    public static void sendArrayOfObjects(Object[] a, int proc, int tag) throws MPIException, IOException
    {
        for (int i = 0; i < a.length; i++)
            sendObject(a[i], proc, tag + i);
    }

    public static Object[] recvArrayOfObjects(int proc,int tag) throws MPIException, IOException, ClassNotFoundException
    {
        Object[] o = new Object[4];
        for (int i = 0; i < 4; i++)
            o[i] = recvObject(proc, tag + i);
        return o;
    }

    // run command:
    //   openmpi/bin/mpirun --hostfile hostfile  -np 4 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/SecondModuleTask 2
    // output:
    //    HERE DISPLAYED EXCEPTION, but program works fine and then prints:
    //    Objects successfully received
    //    true
    //    true
    //    false
    //    true
    private static void sendAndReceiveObjects(String args[]) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        if(MPI.COMM_WORLD.getRank() == 1)
        {
            Dog[] dogs = getDogsArray();
            sendObjects(dogs, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            Dog[] recievedDogs = convertObjectsToDogs(recvObjects(4,1,1));
            System.out.println("Objects successfully received");

            showDogsProperties(recievedDogs);
        }

        MPI.Finalize();
    }

    public static void sendObjects(Object[] a, int proc, int tag)throws MPIException
    {
        ByteArrayOutputStream bos = null;
        try
        {
            bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            for (int i = 0; i < a.length; i++)
                oos.writeObject(a[i]);

            bos.toByteArray();
        }
        catch (Exception ex) {
            Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] temp = bos.toByteArray();
        ByteBuffer buf = MPI.newByteBuffer(temp.length);
        buf.put(temp);
        MPI.COMM_WORLD.iSend(buf, temp.length, MPI.BYTE, proc, tag);
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

    // run command:
    //   openmpi/bin/mpirun --hostfile hostfile  -np 4 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/SecondModuleTask 2
    // output:
    //    true
    //    I'm 3 processor trying to send
    //    true
    //    true
    //    I'm 0 processor trying to send
    //    I'm 2 processor trying to send
    //    true
    //    I'm 1 processor trying to send
    //    I'm 3 received dog
    //    I'm 0 received dog
    //    true
    //    true
    //    I'm 2 received dog
    //    true
    //    I'm 1 received dog
    //    true
    private static void sendObjectWithBcast(String args[]) throws MPIException, IOException, ClassNotFoundException{
        MPI.Init(args);

        Dog dog = new Dog(true);
        System.out.println(dog.DoesEatCats());
        System.out.println("I'm " + MPI.COMM_WORLD.getRank() + " processor trying to send");

        Dog receivedDog = (Dog) bcastObject(dog, MPI.COMM_WORLD.getRank());
        System.out.println("I'm " + MPI.COMM_WORLD.getRank() +" received dog");
        System.out.println(receivedDog.DoesEatCats());

        MPI.Finalize();
    }

    public static Object bcastObject(Object o, int root)throws IOException,MPIException,ClassNotFoundException
    {
        byte []tmp=null;
        int []size=new int[1];
        int rank=MPI.COMM_WORLD.getRank();
        if (rank==root)
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            tmp=bos.toByteArray();
            size[0]=tmp.length;
        }
        MPI.COMM_WORLD.bcast(size,1,MPI.INT,root);
        if (rank!=root) tmp=new byte[size[0]];
        MPI.COMM_WORLD.bcast(tmp,tmp.length,MPI.BYTE,root);
        if (rank!=root)
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        }
        return o;
    }

    // run command:
    //   openmpi/bin/mpirun --hostfile hostfile  -np 4 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/SecondModuleTask 2
    // output:
    //    I'm 3 processor trying to send dogs array
    //    I'm 1 processor trying to send dogs array
    //    I'm 0 processor trying to send dogs array
    //    I'm 2 processor trying to send dogs array
    //    Program successfully finished. My rank = 3
    //    Program successfully finished. My rank = 0
    //    Program successfully finished. My rank = 1
    //    Program successfully finished. My rank = 2
    private static void sendObjectsWithBcast(String args[]) throws MPIException, IOException, ClassNotFoundException{
        MPI.Init(args);

        Dog[] dogs = getDogsArray();
        System.out.println("I'm " + MPI.COMM_WORLD.getRank() + " processor trying to send dogs array");

        bcastObjectArray(dogs, dogs.length, MPI.COMM_WORLD.getRank());

        System.out.println("Program successfully finished. My rank = " + MPI.COMM_WORLD.getRank());

        MPI.Finalize();
    }

    public static void bcastObjectArray(Object []o, int count, int root)throws IOException, MPIException,ClassNotFoundException
    {
        byte []tmp=null;
        int []size=new int[1];
        int rank=MPI.COMM_WORLD.getRank();
        if (rank==root)
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            for (int i=0; i<count; i++) oos.writeObject(o[i]);
            tmp=bos.toByteArray();
            size[0]=tmp.length;
        }
        MPI.COMM_WORLD.bcast(size,1,MPI.INT,root);
        if (rank!=root) tmp=new byte[size[0]];
        MPI.COMM_WORLD.bcast(tmp,tmp.length,MPI.BYTE,root);
        if (rank!=root)
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            for (int i=0; i<count; i++) o[i]=ois.readObject();
        }
    }

    private static Dog[] getDogsArray(){
        Dog dog = new Dog(true);
        Dog dog2 = new Dog(true);
        Dog dog3 = new Dog(false);
        Dog dog4 = new Dog(true);
        Dog[] dogs  = {dog, dog2, dog3, dog4};

        return dogs;
    }

    private static void showDogsProperties(Dog[] dogs){
        for (Dog dog:dogs) {
            System.out.println(dog.DoesEatCats());
        }
    }

    private static Dog[] convertObjectsToDogs(Object[] objects){
        Dog[] dogs = new Dog[objects.length];

        for (int i=0; i<objects.length; ++i){
            dogs[i] = (Dog) objects[i];
        }

        return dogs;
    }

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        // sendAndReceiveObject(args);
        // sendAndReceiveArrayOfObjects(args);
        // sendAndReceiveObjects(args);
        // sendObjectWithBcast(args);
        sendObjectsWithBcast(args);
    }
}

class Dog implements Serializable
{
    boolean _eatCats;

    Dog(boolean eatCats)
    {
        _eatCats = eatCats;
    }

    public boolean DoesEatCats()
    {
        return _eatCats;
    }
}
