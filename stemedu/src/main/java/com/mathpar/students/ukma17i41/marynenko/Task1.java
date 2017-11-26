package com.mathpar.students.ukma17i41.marynenko;

import mpi.MPI;
import mpi.MPIException;

/**
 * /users/anja/openmpi/bin/mpirun -np 2 java -cp /Users/anja/Downloads/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/marynenko/Task1
 * @author anja marynenko
 */

/*
    Task 1
    
    Output:
    Proc num 0 Hello World
    Proc num 1 Hello World
    */
    public class Task1 {
        public static void Task1(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        //end of the parallel part
        MPI.Finalize();
        }
        
        public static void main(String[] args) throws MPIException {
        Task1(args);
        }
    }