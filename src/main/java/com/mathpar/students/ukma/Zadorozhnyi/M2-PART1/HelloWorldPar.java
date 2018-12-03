package com.mathpar.students.ukma.Zadorozhnyi.M2-PART1;
import mpi.*;

public class HelloWorldPar {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        MPI.Finalize();
    }

}

// Output for amount of processors equals 4
//        Proc num 1 Hello World
//        Proc num 2 Hello World
//        Proc num 0 Hello World
//        Proc num 3 Hello World
