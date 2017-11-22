/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.beyrak;

import com.mathpar.students.llp2.student.helloworldmpi.Transport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import static com.mathpar.students.ukma17i41.beyrak.Helpers.*;

/**
 *
 * @author maria
 */

/*
To run: 
openmpi/bin/mpirun --hostfile hostfile  -np 4 java -cp /home/maria/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/beyrak/Tasks24 
*/

public class Tasks24 {
    
    /*
    Task 1
    
    Output:
    Maria is send
    Object was sent
    Maria is received
    */
    
    public static void Task1(String args[]) 
            throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        if(MPI.COMM_WORLD.getRank() == 1)
        {
            Person personToSend = new Person("Maria");
            System.out.println(personToSend.getName() + " is send");
            sendObject(personToSend, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            Person personToReceive = (Person) recvObject(1, 1);
            System.out.println(personToReceive.getName() + " is received");
        }

        MPI.Finalize();
    }
    
    public static void sendObject(Object a, int proc, int tag) 
            throws MPIException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
        System.out.println("Object was sent");
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
    Task 2

    Output:
    Object was sent
    Object was sent
    Object was sent
    Object was sent
    Array of objects was received
    Maria
    Oleka
    Anastasia
    Kateryna
    */
    
    public static void Task2(String args[]) 
            throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        int myRank=MPI.COMM_WORLD.getRank();

        if(myRank == 1)
        {
            Person[] personsToSent = fillPersonsArray();
            sendArrayOfObjects(personsToSent, 3, 1);
        }
        if(myRank == 3)
        {
            Person[] personsToRecieve = convertObjectsToPersons(recvArrayOfObjects(1,1));
            System.out.println("Array of objects was received");
            showPersonsInfo(personsToRecieve);
        }

        MPI.Finalize();
    }
    
    public static void sendArrayOfObjects(Object[] a, int proc, int tag)
            throws MPIException, IOException {
        for (int i = 0; i < a.length; i++) {
            sendObject(a[i], proc, tag + i);
        }
    }

    public static Object[] recvArrayOfObjects(int proc, int tag) 
            throws MPIException, IOException, ClassNotFoundException {
        Object[] o = new Object[4];
        for (int i = 0; i < 4; i++) {
            o[i] = recvObject(proc, tag + i);
        }
        return o;
    }

    /*
    Task 3

    Output:
    Array of objects was received
    Maria
    Oleka
    Anastasia
    Kateryna
    */
    
    public static void Task3(String args[]) 
            throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        int myRank=MPI.COMM_WORLD.getRank();

        if(myRank == 1)
        {
            Person[] personsToSent = fillPersonsArray();
            sendObjects(personsToSent, 3, 1);
        }
        if(myRank == 3)
        {
            Person[] personsToRecieve = convertObjectsToPersons(recvObjects(4,1,1));
            System.out.println("Array of objects was received");
            showPersonsInfo(personsToRecieve);
        }

        MPI.Finalize();
    }
    
    public static void sendObjects(Object[] a, int proc, int tag) 
            throws MPIException {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ObjectOutputStream oos
                    = new ObjectOutputStream(bos);
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

    public static Object[] recvObjects(int m, int proc, int tag) 
            throws MPIException
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
    
    /*
    Task 4

    Output:
    0 processor sent person with name Maria
    1 processor sent person with name Maria
    2 processor sent person with name Maria
    3 processor sent person with name Maria
    
    0 processor receive person with name Maria
    2 processor receive person with name Maria
    3 processor receive person with name Maria
    1 processor receive person with name Maria
    */
    
    public static void Task4(String args[]) 
            throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        Person personToSent = new Person("Maria");
        System.out.println(
                MPI.COMM_WORLD.getRank() + 
                " processor sent person with name " + 
                personToSent.getName());

        Person personToReceive = (Person) bcastObject(personToSent, MPI.COMM_WORLD.getRank());
        System.out.println(
                MPI.COMM_WORLD.getRank() + 
                " processor receive person with name " + 
                personToReceive.getName());

        MPI.Finalize();
    }
    
    public static Object bcastObject(Object o, int root) 
            throws IOException, MPIException, ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == root) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
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
    
    /*
    Task 5
    
    Output:
    0 processor send persons array
    3 processor send persons array
    2 processor send persons array
    1 processor send persons array
    0 processor receive persons array
    3 processor receive persons array
    1 processor receive persons array
    2 processor receive persons array
    */
    
    public static void Task5(String args[]) 
            throws MPIException, IOException, ClassNotFoundException{
        MPI.Init(args);

        Person[] persons = fillPersonsArray();
        System.out.println(MPI.COMM_WORLD.getRank() + " processor send persons array");

        bcastObjectArray(persons, persons.length, MPI.COMM_WORLD.getRank());

        System.out.println(MPI.COMM_WORLD.getRank() + " processor receive persons array");

        MPI.Finalize();
    }
    
    public static void bcastObjectArray(Object[] o, int count, int root) 
            throws IOException, MPIException, ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == root) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
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
    
    
    public static void main(String[] args) 
            throws MPIException, IOException, ClassNotFoundException {
        Task1(args);
        //Task2(args);
        //Task3(args);
        //Task4(args);
        //Task5(args);
    }
}
