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
    task 1
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
    task 2
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
    task 3
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
    task 4
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
    task 5
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
         Task1(args); //openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/movchan/Tasks 3
        // Task2(args); //openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/movchan/Tasks 3
        // Task3(args); //openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/movchan/Tasks 3
        // Task4(args); //openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/movchan/Tasks 3
        // Task5(args); //openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/movchan/Tasks 3
    }
    
}
