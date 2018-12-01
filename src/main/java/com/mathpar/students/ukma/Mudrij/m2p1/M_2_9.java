/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p1;

import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author vmudrij
 */
// com.mathpar.students.ukma.Zhyrkova.Module2Part1.ZhyrkovaTestAllGatherv
public class M_2_9 {
    public static void main(String[] args) throws MPIException, InterruptedException {
        // Initialization MPI
        MPI.Init(args);

        // Definition a processor amount
        int myrank = MPI.COMM_WORLD.getRank();

        // Definition a processor amount in a group
        int np = MPI.COMM_WORLD.getSize();

        // Input parameter - an array size
        int n = 2;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = i;
        }

        int[] q = new int[n * np];

        MPI.COMM_WORLD.allGatherv(a, n, MPI.INT, q, new int[] {n, n}, new int[] {0, 2}, MPI.INT);

        Thread.sleep(60 * myrank);

        System.out.println("myrank " + myrank + " : ");

        if (myrank == np - 1) {
            for (int i = 0; i < q.length; i++) {
                System.out.println(" " + q[i]);
            }
        }

        System.out.println();

        // Completion a parallel part
        MPI.Finalize();
    }
}

// Output:
//        myrank 0 :
//
//        myrank 1 :
//        0
//        1
//        0
//        1
