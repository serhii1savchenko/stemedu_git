/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p1;

import java.math.BigInteger;
import java.util.Random;
import mpi.*;

/**
 *
 * @author vmudrij
 */
// TestBcast
public class M_2_5 {
    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);

        // Determine the number of processors
        int myrank = MPI.COMM_WORLD.getRank();

        // Input parameter - an array size
        int n = 4;

        if (args.length != 0) {
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

        MPI.Finalize();
    }
}

// Output:
// a[0]= 0.44425017395071087
// a[1]= 0.7786018081759669
// a[2]= 0.7845693804372024
// a[3]= 0.12285034656396099
// a[0]= 0.44425017395071087
// a[1]= 0.7786018081759669
// a[2]= 0.7845693804372024
// a[3]= 0.12285034656396099
// a[0]= 0.44425017395071087
// a[1]= 0.7786018081759669
// a[2]= 0.7845693804372024
// a[3]= 0.12285034656396099
// a[0]= 0.44425017395071087
// a[1]= 0.7786018081759669
// a[2]= 0.7845693804372024
// a[3]= 0.12285034656396099
