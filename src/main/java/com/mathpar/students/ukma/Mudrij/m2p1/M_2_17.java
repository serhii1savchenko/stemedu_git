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
// TestScan
public class M_2_17 {
    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);

        // Definition a processor amount
        int myrank = MPI.COMM_WORLD.getRank();

        // Definition a processor amount in a group
        int np = MPI.COMM_WORLD.getSize();
        int n = 2;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = i;
        }

        int[] q = new int[n];

        MPI.COMM_WORLD.scan(a, q, n, MPI.INT, MPI.SUM);

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

// OUTPUT:
// myrank = 0
// 0
// myrank = 1
// 1
// 0
// 2
