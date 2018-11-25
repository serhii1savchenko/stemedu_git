package com.mathpar.students.ukma.Bohachek;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class MPI_3_9_AllToAllv {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = myrank;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n];
        MPI.COMM_WORLD.allToAllv(a, new int[]{1, 1,1,1},
                new int[]{0,1, 2,3}, MPI.INT, q,
                new int[]{1, 1,1,1}, new int[]{0, 1,2,3}, MPI.INT);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}

/*
mpirun --hostfile /home/elizabeth/hostfile -np 4 java -cp /home/elizabeth/stemedu/target/classes com.mathpar.students.ukma.Bohachek/MPI_3_9_AllToAllv 4
myrank = 2: a = [2, 2, 2, 2]
myrank = 1: a = [1, 1, 1, 1]
myrank = 0: a = [0, 0, 0, 0]
myrank = 3: a = [3, 3, 3, 3]
myrank = 3: q = [0, 1, 2, 3]
myrank = 0: q = [0, 1, 2, 3]
myrank = 1: q = [0, 1, 2, 3]
myrank = 2: q = [0, 1, 2, 3]
*/