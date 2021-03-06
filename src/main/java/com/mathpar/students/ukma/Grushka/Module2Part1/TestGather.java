package com.mathpar.students.ukma.Grushka.Module2Part1;

import mpi.*;

public class TestGather {
    public static void main(String[] args) throws Exception {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors and processor amount in a group
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        // Input parameter - an array size
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = myrank;
        int[] q = new int[n * np];
        MPI.COMM_WORLD.gather(a, n, MPI.INT, q, n, MPI.INT, np - 1);
        if (myrank == np - 1) {
            for (int i = 0; i < q.length; i++)
                System.out.println(" " + q[i]);
        }

        MPI.Finalize();
    }
}


//mpirun -np 3 java -cp out/production/Module2Part1 TestGather

//  Result (3 processors & n=4):
//        0
//        0
//        0
//        0
//        1
//        1
//        1
//        1
//        2
//        2
//        2
//        2





