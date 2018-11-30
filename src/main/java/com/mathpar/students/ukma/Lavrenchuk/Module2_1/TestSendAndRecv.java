package com.mathpar.students.ukma.Lavrenchuk.module2_1;

import java.util.Random;
import mpi.*;

public class TestSendAndRecv {
    public static void main(String[] args)
            throws MPIException {
        // Initialization MPI
        MPI.Init(args);
        // Definition a processor amount
        final int myrank = MPI.COMM_WORLD.getRank();
        // Definition a processor amount in a group
        final int np = MPI.COMM_WORLD.getSize();
        // Input parameter - an array size
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        double[] a = new double[n];
        // Processors synchronization
        MPI.COMM_WORLD.barrier();
        // If the processor's number equals 0
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = (new Random()).nextDouble();
                System.out.println("a[" + i + "]= " + a[i]);
            }
            // Sending elements by 0s processor
            for (int i = 1; i < np; i++) {
                MPI.COMM_WORLD.send(a, n, MPI.DOUBLE, i, 3000);
            }
            System.out.println("Proc num " + myrank +
                    " array send" + "\n");
        } else {
            // Getting a message to a processor with number i from processor with number 0
            MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
            System.out.println("Proc num " + myrank +
                    " array recieved" + "\n");
        }
        // Completion a parallel part
        MPI.Finalize();
    }
}

//        OUTPUT:
//        a[0]= 0.6204243874413592
//        a[1]= 0.13248427586370348
//        a[2]= 0.24958978082036776
//        a[3]= 0.02554927486304215
//        Proc num 0 array send
//
//        a[0]= 0.6204243874413592
//        a[1]= 0.13248427586370348
//        a[2]= 0.24958978082036776
//        a[3]= 0.02554927486304215
//        Proc num 3 array recieved
//
//        a[0]= 0.6204243874413592
//        a[1]= 0.13248427586370348
//        a[2]= 0.24958978082036776
//        a[3]= 0.02554927486304215
//        Proc num 1 array recieved
//
//        a[0]= 0.6204243874413592
//        a[1]= 0.13248427586370348
//        a[2]= 0.24958978082036776
//        a[3]= 0.02554927486304215
//        Proc num 2 array recieved