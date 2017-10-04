/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.beyrak;

import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author maria
 */

/*
To run:
openmpi/bin/mpirun -np 2 java -cp /home/maria/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/beyrak/Tasks 2
*/
public class Tasks {
    
    /*
    Task 1
    
    Output:
    Proc num 0 Hello World
    Proc num 1 Hello World
    */
    public static void Task1(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        //end of the parallel part
        MPI.Finalize();
    }
    
    /*
    Task 2
    
    Output:
    */
    
    public static void Task2(String[] args) throws MPIException {
        System.out.println("Task 2");
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
                    + " Array was sent" + "\n");
        } else {
            //i-th processor receives messages from the processor with number 0
            MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
            System.out.println("Proc num " + myrank
                    + " Array was accepted" + "\n");
        }
        //end of the parallel part
        MPI.Finalize();
    }
    
    public static void main(String[] args) throws MPIException {
        //Task1(args);
        Task2(args);
    }
}
