package com.mathpar.students.ukma.Dymchenko;
import mpi.*;

public class Dymchenko1 {
    public static void main(String[] args) throws MPIException{ 
        MPI.Init(args);
        int myRank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myRank + " Hello World");
        MPI.Finalize();
    }
}


// mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko1

// Result (2 processors):
//        Proc num 1 Hello World
//        Proc num 0 Hello World