package com.mathpar.students.ukma.Bohachek;

import java.util.Arrays;
import mpi.*;

public class MPI_3_4_Scatter {
    public static void main(String[] args)
            throws MPIException{
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n*np];
        if (myrank == 0){
            for (int i = 0; i < a.length; i++)
                a[i] = i ;
            System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        }
        int[] q = new int[n];
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatter(a,n, MPI.INT, q,n, MPI.INT, 0);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_3_4_Scatter.java
mpirun java MPI_3_4_Scatter 2

myrank = 0: a = [0, 1, 2, 3]
myrank = 0: q = [0, 1]
myrank = 1: q = [2, 3]

*/