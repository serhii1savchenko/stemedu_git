package com.mathpar.students.ukma.Zhyrkova.Module2Part1;

import mpi.MPI;
import mpi.MPIException;

public class ZhyrkovaTestAllReduce {
    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);
        // Definition a processor amount
        int myrank = MPI.COMM_WORLD.getRank();
        // Definition a processor amount in a group
        int np = MPI.COMM_WORLD.getSize();
        int n = 2;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        int[] q = new int[n];
        MPI.COMM_WORLD.allReduce(a, q, n, MPI.INT, MPI.PROD);
        for (int j = 0; j < np; j++) {
            if (myrank == j) {
                System.out.println("myrank = " + j);
                for (int i = 0; i < q.length; i++) {
                    System.out.println(" " + q[i]);
                }
            }
        }
        // Completion a parallel part
        MPI.Finalize();
    }
}

//mpirun -np 4 java -cp out/production/Module2Part1 ZhyrkovaTestAllReduce

// Output for four processors and n=2
//        myrank = 1
//        myrank = 2
//        myrank = 3
//        0
//        1
//
//        0
//        1
//        0
//        1
//        myrank = 0
//        0
//        1

// Output for two processors and n=2
//        myrank = 0
//        0
//        1
//        myrank = 1
//        0
//        1