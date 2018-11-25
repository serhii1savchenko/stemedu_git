package com.mathpar.students.ukma.Dymchenko;
import mpi.*;

public class Dymchenko1 {
    public static void main(String[] args) throws MPIException { 
        MPI.Init(args);
        int myRank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myRank + " Hello World");
        MPI.Finalize();
    }
}


// mpirun -np 2 java -cp out/production/Dymchenko1

// Result (4 processors):
//        Proc num 3 Hello World
//        Proc num 2 Hello World
//        Proc num 1 Hello World
//        Proc num 0 Hello World