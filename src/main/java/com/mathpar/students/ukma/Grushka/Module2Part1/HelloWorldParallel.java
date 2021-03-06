package com.mathpar.students.ukma.Grushka.Module2Part1;

import mpi.*;

public class HelloWorldParallel {

    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");

        MPI.Finalize();
    }

}

//mpirun -np 4 java -cp out/production/Module2Part1 HelloWorldParallel

// Result (4 processors):
//        Proc num 3 Hello World
//        Proc num 2 Hello World
//        Proc num 1 Hello World
//        Proc num 0 Hello World
