package com.mathpar.students.ukma.Bohachek;

import java.util.Arrays;
import mpi.*;

public class MPI_3_5_Scatterv {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        int np = MPI.COMM_WORLD.getSize();
        int[] a = new int[n*np];
        if(myrank == 0){
            for (int i = 0; i < a.length; i++) a[i] = i;
            System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        }
        int[] q = new int[n];
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatterv(a, new int[]{n, n, n, n},
                new int[]{0, 4, 8, 12}, MPI.INT, q, n, MPI.INT, 0);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}

/*
mpirun --hostfile /home/elizabeth/hostfile -np 4 java -cp /home/elizabeth/stemedu/target/classes com.mathpar.students.ukma.Bohachek/MPI_3_5_Scatterv 4
myrank = 0: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
myrank = 0: q = [0, 1, 2, 3]
myrank = 1: q = [4, 5, 6, 7]
myrank = 3: q = [12, 13, 14, 15]
myrank = 2: q = [8, 9, 10, 11]
*/