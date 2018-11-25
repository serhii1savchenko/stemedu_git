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
 mpirun --hostfile /home/elizabeth/hostfile -np 2 java -cp /home/elizabeth/stemedu/target/classes com/mathpar/students/ukma/Bohachek/HelloWorld


Proc num 0 Hello World
Proc num 1 Hello World
*/