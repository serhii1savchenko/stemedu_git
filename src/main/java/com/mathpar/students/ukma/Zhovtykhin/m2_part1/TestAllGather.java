package com.mathpar.students.ukma.Zhovtykhin.m2_part1;

import mpi.*;

public class TestAllGather {
    public static void main(String[] args) throws Exception {
        // Initialization MPI
        MPI.Init(args);
        Thread t = new Thread();
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
        MPI.COMM_WORLD.allGather(a, n, MPI.INT, q, n, MPI.INT);
        t.sleep(60 * myrank);
        System.out.println("myrank = " + myrank + " : ");
        for (int i = 0; i < q.length; i++) {
            System.out.println(" " + q[i]);
        }
        System.out.println();
        // Completion a parallel part
        MPI.Finalize();
    }
}

// Output for amount of processors equals 2 and n=4
//        myrank = 0 :
//        0
//        0
//        0
//        0
//        1
//        1
//        1
//        1
//
//        myrank = 1 :
//        0
//        0
//        0
//        0
//        1
//        1
//        1
//        1
