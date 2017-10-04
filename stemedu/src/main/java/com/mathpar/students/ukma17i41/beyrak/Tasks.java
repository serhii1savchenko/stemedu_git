/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.beyrak;

import java.nio.IntBuffer;
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
    a[0]= 0.5288058984918489
    a[1]= 0.30107803618513407
    Proc num 0: Array was sent
    
    a[0]= 0.5288058984918489
    a[1]= 0.30107803618513407
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
    To run:
    openmpi/bin/mpirun -np 2 java -cp /home/maria/stemedu/stemedu/target/classes: com/mathpar/students/ukma17i41/beyrak/Tasks 1000
    1000 - number of elements in the transferred array
    
    Task 3
    
    Output:
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
    
    
    public static void main(String[] args) throws MPIException {
        //Task1(args);
        //Task2(args);
        Task3(args);
    }
}
