package com.mathpar.students.ukma.Tsukanova;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class MPI3_13 {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        var size = MPI.COMM_WORLD.getSize();
        var recvSizes = new int[size];
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
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI3_13 4

RESULT
myrank = 2: a = [0, 1, 2, 3]
myrank = 3: a = [0, 1, 2, 3]
myrank = 0: a = [0, 1, 2, 3]
myrank = 1: a = [0, 1, 2, 3]
myrank = 0: q = [0, 0, 0, 0]

*/