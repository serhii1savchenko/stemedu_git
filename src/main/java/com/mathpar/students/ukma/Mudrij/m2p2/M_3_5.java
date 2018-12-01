/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p2;

import java.util.Arrays;
import mpi.*;

/**
 *
 * @author vmudrij
 */
// com.mathpar.students.ukma.Zhyrkova.Module2Part1.ZhyrkovaTestScatterv
public class M_3_5 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.getRank();
        int n = 4;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        int np = MPI.COMM_WORLD.getSize();
        int[] a = new int[n * np];

        if (myrank == 0) {
            for (int i = 0; i < a.length; i++) {
                a[i] = i;
            }

            System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        }
        int[] q = new int[n];

        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatterv(a, new int[] {n, n, n, n},
                new int[] {0, 4, 8, 12}, MPI.INT, q, n, MPI.INT, 0);

        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));

        MPI.Finalize();
    }
}

// OUTPUT:
// myrank = 0: a = [0, 1, 2, 3, 4, 5, 6, 7]
// myrank = 0: q = [0, 1, 2, 3]
// myrank = 1: q = [4, 5, 6, 7]