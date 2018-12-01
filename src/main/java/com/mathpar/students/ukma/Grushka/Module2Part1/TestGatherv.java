package com.mathpar.students.ukma.Grushka.Module2Part1;

import mpi.MPI;
import mpi.MPIException;

public class TestGatherv {
    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors and processor amount in a group
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

        MPI.Finalize();
    }

}

//  Result (2 processors & n=4):
//        0
//        0
//        1
//        1
//        1
//        1
//        0
//        0
