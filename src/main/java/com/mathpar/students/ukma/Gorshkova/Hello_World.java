package com.mathpar.students.ukma.Gorshkova;

import mpi.*;

public class Hello_World {
    public static void main(String[] args) throws MPIException { 
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
         
        MPI.Finalize();
    }
}
