package com.mathpar.students.ukma.Bohachek;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class MPI3_13_ReduceScatter {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        int size = MPI.COMM_WORLD.getSize();
        int[] recvSizes = new int[size];
        Arrays.fill(recvSizes, 1);
        for (int i = 0; i < n; i++) a[i] = i;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n];
        MPI.COMM_WORLD.reduceScatter(a, q,
                recvSizes, MPI.INT, MPI.SUM);
        if (myrank == 0)
            System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI3_13_ReduceScatter.java
mpirun java MPI3_13_ReduceScatter 3

myrank = 0: a = [0, 1, 2]
myrank = 2: a = [0, 1, 2]
myrank = 1: a = [0, 1, 2]
myrank = 0: q = [0, 0, 0]


*/