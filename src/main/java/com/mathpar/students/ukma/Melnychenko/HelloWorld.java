package com.mathpar.students.ukma.Bohachek;

import mpi.*;

public class HelloWorld{
public static void main(String[] args)
    throws MPIException { 
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");

        MPI.Finalize();
    }
}

/*
googbyeworld
mpijavac -cp "/usr/local/lib/mpi.jar" HelloWorld.java
mpirun java HelloWorld


Proc num 0 Hello World
Proc num 1 Hello World
*/