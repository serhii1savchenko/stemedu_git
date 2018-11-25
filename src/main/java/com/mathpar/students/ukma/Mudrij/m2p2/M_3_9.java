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
// TestAllToAllv
public class M_3_9 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.getRank();
        int n = 4;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = myrank;
        }

        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));

        int[] q = new int[n];

        MPI.COMM_WORLD.allToAllv(a,
                new int[] {1, 1, 1, 1},
                new int[] {0, 1, 2, 3}, MPI.INT, q,
                new int[] {1, 1, 1, 1},
                new int[] {0, 1, 2, 3}, MPI.INT);

        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));

        MPI.Finalize();
    }
}

// Output:
// myrank = 0: a = [0, 0, 0, 0]
// myrank = 1: a = [1, 1, 1, 1]
// myrank = 0: q = [0, 1, 0, 0]
// myrank = 1: q = [0, 1, 0, 0]
