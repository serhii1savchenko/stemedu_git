/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mathpar.students.ukma17i41.movchan;

import java.nio.IntBuffer;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

/* 
 @author amo
 */
//      Run command: 
//      /usr/bin/mpirun -np 2 java -cp /home/amo/projects/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/movchan/Tasks 3
    
public class Tasks {
    /*
    task 1 openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/movchan/Tasks 5
    */
    /*
    Proc num 1 Hello World
    Proc num 0 Hello World
    */
    
    public static void Task1(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World" + "\n");
        //end of the parallel part
        MPI.Finalize();
    }
    
    /*
    task 2 openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/movchan/Tasks 5
    */
    /*
    a[0]= 0.8116478783553964
    a[1]= 0.1405152683103933
    a[2]= 0.2556516562307126
    a[3]= 0.0990558044747053
    a[4]= 0.960096904623143
    Proc num 0: Array was sent
    a[0]= 0.8116478783553964
    a[1]= 0.1405152683103933
    a[2]= 0.2556516562307126
    a[3]= 0.0990558044747053
    a[4]= 0.960096904623143
    Proc num 1: Array was accepted
    */
    
    public static void Task2(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        //determining the number of processors in a group
        int np = MPI.COMM_WORLD.getSize();
        //array size
        int n = Integer.parseInt(args[0]);
        double[] a = new double[n];
        //processors synchronization
        MPI.COMM_WORLD.barrier();
        //if processor with number 0
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = (new Random()).nextDouble();
                System.out.println("a[" + i + "]= " + a[i]);
            }
            //send elements by processor with number 0
            for (int i = 1; i < np; i++) {
                MPI.COMM_WORLD.send(a, n, MPI.DOUBLE, i, 3000);
            }
            System.out.println("Proc num " + myrank
                    + ": Array was sent" + "\n");
        } else {
            //i-th processor receives messages from the processor with number 0
            MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
            System.out.println("Proc num " + myrank
                    + ": Array was accepted" + "\n");
        }
        //end of the parallel part
        MPI.Finalize();
    }
    
    /*
    task 3 openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/movchan/Tasks 5
    */
    /*
    proc num = 1: Array was accepted
    proc num = 0: Array was sent
    */
    
    public static void Task3(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        //determining the number of processors in a group
        int np = MPI.COMM_WORLD.getSize();
        //array size
        int n = Integer.parseInt(args[0]);
        IntBuffer b = MPI.newIntBuffer(n);
        MPI.COMM_WORLD.barrier();
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                b.put(new Random().nextInt(10));
            }
            for (int i = 1; i < np; i++) {
                MPI.COMM_WORLD.iSend(b, b.capacity(), MPI.INT, i, 3000);
            }
            System.out.println("proc num = " + myrank
                    + ": Array was sent");
        } else {
            MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);
            System.out.println("proc num = " + myrank
                    + ": Array was accepted");
        }
        //end of the parallel part
        MPI.Finalize();
    }
    
    /*
    task 4 openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/movchan/Tasks 5
    */
    /*
    a[0]= 0.13837821696423247
    a[1]= 0.6693324681794728
    a[2]= 0.12287262094033158
    a[3]= 0.03577005627102792
    a[4]= 0.0698539097109886
    a[0]= 0.13837821696423247
    a[1]= 0.6693324681794728
    a[2]= 0.12287262094033158
    a[3]= 0.03577005627102792
    a[4]= 0.0698539097109886
    */
    
    public static void Task4(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        double[] a = new double[n];
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        MPI.COMM_WORLD.barrier();
        //send data from 0-th processor to others
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0) {
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        //end of the parallel part
        MPI.Finalize();
    }
    
    /*
    task 5 openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/movchan/Tasks 5
    */
    /*
    0
    0
    0
    0
    0
    1
    1
    1
    1
    1
    */
    
    public static void Task5(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        //determining the number of processors in a group
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = myrank;
        }
        int[] q = new int[n * np];
        MPI.COMM_WORLD.gather(a, n, MPI.INT, q, n, MPI.INT, np - 1);
        if (myrank == np - 1) {
            for (int i = 0; i < q.length; i++) {
                System.out.println(" " + q[i]);
            }
        }
        //end of the parallel part
        MPI.Finalize();
    }
    
    public static void main(String[] args) throws MPIException {
        // Task1(args);
        // Task2(args);
        // Task3(args);
        // Task4(args);
        // Task5(args);
    }
    
}
