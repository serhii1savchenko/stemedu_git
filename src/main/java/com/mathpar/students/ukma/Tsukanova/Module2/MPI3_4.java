package com.mathpar.students.ukma.Tsukanova;

import java.util.Arrays;
import mpi.*;

public class MPI3_4 {
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
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 2 java -cp out/production/HelloWorld MPI3_4 4

RESULT
myrank = 0: a = [0, 0, 0, 0]
myrank = 1: a = [1, 1, 1, 1]
myrank = 1: q = [0, 0, 1, 1, 1, 1, 0, 0]

*/