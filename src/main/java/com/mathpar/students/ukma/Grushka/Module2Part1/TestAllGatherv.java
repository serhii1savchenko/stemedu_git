package com.mathpar.students.ukma.Grushka.Module2Part1;

import mpi.MPI;
import mpi.MPIException;

public class TestAllGatherv {

    public static void main(String[] args) throws MPIException, InterruptedException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors and processor amount in a group
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        // Input an array size
        int n = 4;
        if (args.length != 0)
           n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        int[] q = new int[n * np];
        MPI.COMM_WORLD.allGatherv(a, n, MPI.INT, q, new int[]{n, n}, new int[]{0, 2}, MPI.INT);

            Thread.sleep(60 * myrank);
            System.out.println("myrank " + myrank + " : ");
            if (myrank == np - 1) {
                for (int i = 0; i < q.length; i++) {
                    System.out.println(" " + q[i]);
                }
            }
            System.out.println();

            MPI.Finalize();
        }
    }


//mpirun -np 2 java -cp out/production/Module2Part1 TestAllGatherv

// Result (2 processors & n=4):
//        myrank 0 :
//
//        myrank 1 :
//        0
//        1
//        0
//        1
