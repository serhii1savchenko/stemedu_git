/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p1;

import mpi.*;

/**
 *
 * @author vmudrij
 */
// com.mathpar.students.ukma.Zhyrkova.Module2Part1.ZhyrkovaTestGather
public class M_2_6 {
    public static void main(String[] args) throws Exception {
        // Initialization MPI
        MPI.Init(args);

        // Definition a processor amount
        int myrank = MPI.COMM_WORLD.getRank();

        // Definition a processor amount in a group
        int np = MPI.COMM_WORLD.getSize();

        // Input parameter - an array size
        int n = 4;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = myrank;
        }

        int[] q = new int[n * np];

        MPI.COMM_WORLD.gather(a, n, MPI.INT, q, n, MPI.INT, np - 1);

        if (myrank == np - 1) {
            for (int i = 0; i < q.length; i++) {
                System.out.println(" " + q[i]);
            }
        }

        // Completion a parallel part
        MPI.Finalize();
    }
}

// Output:
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
//        3
//        3
//        3
//        3
