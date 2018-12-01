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
// com.mathpar.students.ukma.Zhyrkova.Module2Part1.ZhyrkovaTestGather
public class M_3_2 {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = 4;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = myrank;
        }

        System.out.println("myrank = " + myrank + " : a = " + Arrays.toString(a));

        int[] q = new int[n * np];

        MPI.COMM_WORLD.gather(a, n, MPI.INT, q, n, MPI.INT, np - 1);

        if (myrank == np - 1) {
            System.out.println("myrank = " + myrank + " : q = " + Arrays.toString(q));
        }

        MPI.Finalize();
    }
}

// Output
// myrank = 2 : a = [2, 2, 2, 2]
// myrank = 1 : a = [1, 1, 1, 1]
// myrank = 3 : a = [3, 3, 3, 3]
// myrank = 0 : a = [0, 0, 0, 0]
// myrank = 3 : q = [0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3]
