package com.mathpar.students.ukma.Gorshkova;


import mpi.*;

public class Task2_1 {

    public static void main(String[] args) throws MPIException
    {
            MPI.Init(args);

            int myRank = MPI.COMM_WORLD.getRank();

            System.out.println(String.format("Proc num %s Hello World", myRank));

            MPI.Finalize();
    }
}
