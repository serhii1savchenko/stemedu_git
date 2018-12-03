package com.mathpar.students.ukma.Fedorak.Module2;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class MPI3_12 {
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
        MPI.COMM_WORLD.allReduce(a, q, n, MPI.INT, MPI.PROD);
        for (int j = 0; j < np; j++)
            if (myrank == j) {
                System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
            }
        MPI.Finalize();
    }
}

/*
mpirun -np 4 --hostfile hostfile java -cp class com.mathpar.students.ukma.Fedorak.Module2.MPI3_12 10 

myrank = 2: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
myrank = 0: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
myrank = 3: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
myrank = 1: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
myrank = 1: q = [0, 1, 16, 81, 256, 625, 1296, 2401, 4096, 6561]myrank = 0: q = [0, 1, 16, 81, 256, 625, 1296, 2401, 4096, 6561]

myrank = 3: q = [0, 1, 16, 81, 256, 625, 1296, 2401, 4096, 6561]myrank = 2: q = [0, 1, 16, 81, 256, 625, 1296, 2401, 4096, 6561]

*/
