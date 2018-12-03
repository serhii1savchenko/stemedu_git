package com.mathpar.students.ukma.lysenko;

import mpi.*;

public class HelloWorld {
    public static void main(String[] args) throws MPIException { 
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
         
        MPI.Finalize();
    }
}

//mpirun -np 2 java -cp /home/teacher/stemedu/target/classes com.mathpar.students.ukma.Kladko/HelloWorld