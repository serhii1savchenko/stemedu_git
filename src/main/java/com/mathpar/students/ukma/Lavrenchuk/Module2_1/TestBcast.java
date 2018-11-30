package com.mathpar.students.ukma.Lavrenchuk.module2_1;

import java.util.Random;
import mpi.*;

/**
 * The processor with the number of 0 is sending an array of numbers
 * to other processors,
 * using distribution on a binary tree.
 */
public class TestBcast {
    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);
        // Definition a processor amount
        int myrank = MPI.COMM_WORLD.getRank();
        // Input parameter - an array size
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        double[] a = new double[n];
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        MPI.COMM_WORLD.barrier();
        // Sending a data from processor with the number of 0 to others
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0) {
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        // Completion a parallel part
        MPI.Finalize();
    }
}

//        OUTPUT:
//        a[0]= 0.44425017395071087
//        a[1]= 0.7786018081759669
//        a[2]= 0.7845693804372024
//        a[3]= 0.12285034656396099
//        a[0]= 0.44425017395071087
//        a[1]= 0.7786018081759669
//        a[2]= 0.7845693804372024
//        a[3]= 0.12285034656396099
//        a[0]= 0.44425017395071087
//        a[1]= 0.7786018081759669
//        a[2]= 0.7845693804372024
//        a[3]= 0.12285034656396099
//        a[0]= 0.44425017395071087
//        a[1]= 0.7786018081759669
//        a[2]= 0.7845693804372024
//        a[3]= 0.12285034656396099