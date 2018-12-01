/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p2;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author vmudrij
 */
// com.mathpar.students.ukma.Zhyrkova.Module2Part1.ZhyrkovaTestScan
public class M_3_14 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = 2;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = i;
        }

        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));

        int[] q = new int[n];

        MPI.COMM_WORLD.scan(a, q, n, MPI.INT, MPI.SUM);

        for (int j = 0; j < np; j++) {
            if (myrank == j) {
                System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
            }
        }

        MPI.Finalize();
    }
}

// OUTPUT:
// myrank = 1: a = [0, 1]
// myrank = 0: a = [0, 1]
// myrank = 1: q = [0, 2]
// myrank = 0: q = [0, 1]
