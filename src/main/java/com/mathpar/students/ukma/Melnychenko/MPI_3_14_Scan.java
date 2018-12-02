package com.mathpar.students.ukma.Bohachek;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class MPI_3_14_Scan {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n];
        MPI.COMM_WORLD.scan(a, q, n, MPI.INT, MPI.SUM);
        for (int j = 0; j < np; j++)
            if (myrank == j) {
                System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
            }
        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_3_14_Scan.java
mpirun java MPI_3_14_Scan 3

myrank = 2: a = [0, 1, 2]
myrank = 1: a = [0, 1, 2]
myrank = 0: a = [0, 1, 2]
myrank = 1: q = [0, 2, 4]
myrank = 2: q = [0, 3, 6]
myrank = 0: q = [0, 1, 2]

*/