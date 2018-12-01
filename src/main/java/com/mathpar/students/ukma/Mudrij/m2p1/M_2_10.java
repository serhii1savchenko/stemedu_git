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
// com.mathpar.students.ukma.Zhyrkova.Module2Part1.ZhyrkovaTestScatter
public class M_2_10 {
    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);

        // Definition a processor amount
        int myrank = MPI.COMM_WORLD.getRank();

        // Declaration an array of objects
        int[] a = new int[2];

        // Filling these array on a first processor
        if (myrank == 0) {
            for (int i = 0; i < 2; i++) {
                a[i] = i;
                System.out.println("a = " + a[i] + " ");
            }

            System.out.println("rank = " + myrank);
        }

        // Declaration an array in which elements accepted by the processor
        // will be recorded
        int[] q = new int[2];

        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatter(a, 1, MPI.INT, q, 1, MPI.INT, 0);

        // Printing received arrays and numbers of processors
        for (int i = 0; i < q.length; i++) {
            System.out.println("myrank = " + myrank + " ; " + q[i] + "\n");
        }

        // Completion a parallel part
        MPI.Finalize();
    }
}

// Output:
//        a = 0
//        a = 1
//        rank = 0
//        myrank = 0 ; 0
//
//        myrank = 0 ; 0
//
//        myrank = 1 ; 1
//
//        myrank = 1 ; 0
