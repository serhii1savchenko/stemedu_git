package com.mathpar.students.ukma.Zhyrkova.Module2Part1;

import mpi.MPI;
import mpi.MPIException;

public class ZhyrkovaTestGatherv {
    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);
        // Definition a processor amount
        int myrank = MPI.COMM_WORLD.getRank();
        // Definition a processor amount in a group
        int np = MPI.COMM_WORLD.getSize();
        // Input parameter - an array size
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = myrank;
        }
        int[] q = new int[n * np];
        MPI.COMM_WORLD.gatherv(a, n, MPI.INT, q, new int[]{n, n}, new int[]{0, 2}, MPI.INT, np - 1);
        if (myrank == np - 1) {
            for (int i = 0; i < q.length; i++) {
                System.out.println(" " + q[i]);
            }
        }
        // Completion a parallel part
        MPI.Finalize();
    }

}

//mpirun -np 2 java -cp out/production/Module2Part1 ZhyrkovaTestGatherv

// Output for amount of processors equals 2 and n=4
//        0
//        0
//        1
//        1
//        1
//        1
//        0
//        0