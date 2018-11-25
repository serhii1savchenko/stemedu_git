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
// TestAllGather
public class M_2_8 {
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

        if (args.length != 0) {
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

// Output:
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
