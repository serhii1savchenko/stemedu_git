package com.mathpar.students.ukma.Bohachek;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class MPI_3_7_AllGatherv {
    public static void main(String[] args)
            throws MPIException, InterruptedException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n*np];
        MPI.COMM_WORLD.allGatherv(a, n, MPI.INT,
                q, new int[]{n, n}, new int[]{2, 0}, MPI.INT);
        Thread.sleep(60 * myrank);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}

/*
mpirun --hostfile /home/elizabeth/hostfile -np 2 java -cp /home/elizabeth/stemedu/target/classes com.mathpar.students.ukma.Bohachek/MPI_3_7_AllGatherv 2
myrank = 0: a = [0, 1]
myrank = 1: a = [0, 1]
myrank = 0: q = [0, 1, 0, 1]
myrank = 1: q = [0, 1, 0, 1]

*/