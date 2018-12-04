package com.mathpar.students.ukma.Dymchenko;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class Dymchenko2_12 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = i;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n];
        MPI.COMM_WORLD.allReduce(a, q, n, MPI.INT, MPI.PROD);
        for (int j = 0; j < np; j++) {
            if (myrank == j)
                System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        }
        MPI.Finalize();
    }
}

/*
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko2_12 4
myrank = 0: a = [0, 1, 2, 3]
myrank = 1: a = [0, 1, 2, 3]
myrank = 0: q = [0, 1, 4, 9]myrank = 1: q = [0, 1, 4, 9]

*/