package com.mathpar.students.ukma.Grushka.Module2Part1;

import mpi.*;

public class TestScatter {
    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors
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
        for (int i = 0; i < q.length; i++)
            System.out.println("myrank = " + myrank + " ; " + q[i] + "\n");

        MPI.Finalize();
    }
}

//  Result (2 processors):
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

