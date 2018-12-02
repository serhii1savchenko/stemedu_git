package com.mathpar.students.ukma.Lavrenchuk.Module2_1;

import mpi.*;

public class TestAllGather {
    public static void main(String[] args) throws Exception {
        // MPI init
        MPI.Init(args);
        Thread t = new Thread();
        
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
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
        MPI.COMM_WORLD.allGather(a, n, MPI.INT, q, n, MPI.INT);
        t.sleep(60 * myrank);
        System.out.println("myrank = " + myrank + " : ");
        for (int i = 0; i < q.length; i++) {
            System.out.println(" " + q[i]);
        }
        System.out.println();
        MPI.Finalize();
    }
}

//   OUTPUT:
//   myrank = 0 :
//   0
//   0
//   0
//   0
//   1
//   1
//   1
//   1
//
//   myrank = 1 :
//   0
//   0
//   0
//   0
//   1
//   1
//   1
//   1
