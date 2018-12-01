package com.mathpar.students.ukma.Grushka.Module2Part2;

import java.util.Arrays;
import mpi.*;

public class TestAllToAll {
    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = 2;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int [] a = new int[n];
        for(int i = 0; i < n; i++) a[i] = myrank;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n];
        MPI.COMM_WORLD.allToAll(a, 1, MPI.INT, q, 1, MPI.INT);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));

        MPI.Finalize();
    }
}

//Result (2 processors):
//        myrank = 0: a = [0, 0]
//        myrank = 1: a = [1, 1]
//        myrank = 0: q = [0, 1]
//        myrank = 1: q = [0, 1]

