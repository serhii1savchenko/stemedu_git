package com.mathpar.students.ukma.Zhyrkova.Module2Part1;

import mpi.*;

public class ZhyrkovaHelloWorldParallel {

    public static void main(String[] args) throws MPIException {
        // Initialize a parallel part
        MPI.Init(args);
        // Define a number of a process
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        // Completion a parallel part
        MPI.Finalize();
    }

}

// Output for amount of processors equals 4
//        Proc num 1 Hello World
//        Proc num 2 Hello World
//        Proc num 0 Hello World
//        Proc num 3 Hello World
