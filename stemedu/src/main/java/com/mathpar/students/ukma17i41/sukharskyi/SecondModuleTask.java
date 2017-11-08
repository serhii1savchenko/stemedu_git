/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.sukharskyi;

import java.io.*;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Date;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

/**
 *
 * @author probook
 */
public class SecondModuleTask {

    //120746007
    //120566650
    private static void FirstTask(String[] args) throws MPIException
    {
        MPI.Init(args);
        int myRank=MPI.COMM_WORLD.getRank();
        int n=Integer.parseInt(args[0]);
        int []a=new int[n];
        if (myRank==0){
            System.out.println("My ranks is " + myRank);
            Random rnd=new Random();
            System.out.println(n + " random numbers: ");
            for (int i=0; i<n; i++){
                a[i]=rnd.nextInt()%n;
                System.out.print(a[i] + " ");
            }
            System.out.println();
            MPI.COMM_WORLD.send(a, n, MPI.INT, 1, 0);
        }
        if (myRank==1){
            System.out.println("My ranks is " + myRank);
            MPI.COMM_WORLD.recv(a, n, MPI.INT, 0, 0);
            for (int i=0; i<n; i++){
                System.out.print(a[i] + " ");
            }
            System.out.println();
        }
        MPI.Finalize();
    }

    //115514207
    //116958763
    private static void SecondTask(String[] args) throws MPIException
    {
        MPI.Init(args);
        int myRank=MPI.COMM_WORLD.getRank();
        int n=Integer.parseInt(args[0]);
        IntBuffer a = MPI.newIntBuffer(n);
        if (myRank==0){
            System.out.println("My ranks is " + myRank);
            Random rnd=new Random();
            System.out.println(n + " random numbers: ");
            for (int i=0; i<n; i++){
                a.put(rnd.nextInt()%n);
                System.out.print(a.get(i) + " ");
            }
            System.out.println();
            MPI.COMM_WORLD.iSend(a, n, MPI.INT, 1, 0);
        }
        if (myRank==1){
            System.out.println("My ranks is " + myRank);
            MPI.COMM_WORLD.recv(a, n, MPI.INT, 0, 0);
            for (int i=0; i<n; i++){
                System.out.print(a.get(i) + " ");
            }
            System.out.println();
        }
        MPI.Finalize();
    }

    private static void SendNext(String[] args) throws MPIException
    {
        MPI.Init(args);
        
        int myRank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();
        
        int num = Integer.parseInt(args[0]);
        IntBuffer a = MPI.newIntBuffer(1);
        
        int next = (myRank +1) % size;
        int prev = (myRank -1 + size) % size;

        if(myRank == 0)
        {
            a.put(num);

            while(a.get(0) >= -1)
            {
                System.out.println(myRank + " trying to send " + a.get(0) + " to " + next);

                MPI.COMM_WORLD.iSend(a, 1, MPI.INT, next, 0);
                MPI.COMM_WORLD.recv(a, 1, MPI.INT, prev, 0);

                num = a.get(0);

                System.out.println(myRank + " received " + num + " from " + prev);

                if(num == 1)
                {
                    System.out.println("I was first "+ myRank +" i received " + num +". Finish program");
                    a.put(0, -1);
                }
                else
                {
                    a.put(0, num-1);
                }
            }
        }
        else
        {
            while(a.get(0) > -1)
            {
                MPI.COMM_WORLD.recv(a, 1, MPI.INT, prev, 0);

                num = a.get(0);
                System.out.println(myRank + " received " + num + " from " + prev);

                if(num == 1)
                {
                    System.out.println("I was first  "+ myRank +" i received " + a.get(0)+". Finish program");
                    a.put(0, -1);
                }
                else if(num == -1)
                {
                    a.put(0, -1);
                }
                else
                {
                    a.put(0, num-1);
                }

                System.out.println(myRank + " trying to send " + a.get(0) + " to " + next);
                MPI.COMM_WORLD.iSend(a, 1, MPI.INT, next, 0);
            }

        }
        System.out.println("\n" + myRank + " finished execution\n");
        MPI.Finalize();
    }


    // run command:
    //   openmpi/bin/mpirun --hostfile hostfile  -np 4 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/SecondModuleTask 2
    // output:
    //   Object successfully sent
    //   Object successfully sent
    //   Object successfully sent
    //   Object successfully sent
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

    public static void sendObject(Object a, int proc, int tag) throws MPIException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
        System.out.println("Object successfully sent");
    }

    public static Object recvObject(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {
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

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        // SendNext(args);
        // sendAndReceiveObject(args);


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
