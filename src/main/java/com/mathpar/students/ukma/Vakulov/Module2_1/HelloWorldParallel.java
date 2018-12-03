package com.mathpar.students.ukma.Vakulov.Module2_1;
import mpi.*;

public class HelloWorldParallel {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        MPI.Finalize();
    }

}

// Output for 4 processors
//   Proc num 1 Hello World
//   Proc num 2 Hello World
//   Proc num 0 Hello World
//   Proc num 3 Hello World
