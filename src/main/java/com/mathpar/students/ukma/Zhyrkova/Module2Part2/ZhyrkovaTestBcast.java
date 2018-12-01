package com.mathpar.students.ukma.Zhyrkova.Module2Part2;

import java.util.Arrays;
import java.util.Random;
import mpi.*;

/**
 * The processor with the number of 0 is sending an array of numbers
 * to other processors
 * of communicator MPI.COMM_WORLD
 */
public class ZhyrkovaTestBcast {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
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
            }
            System.out.println("myrank = " + myrank + " : a = "
                    + Arrays.toString(a));
        }
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0)
            System.out.println("myrank = " + myrank + " : a = "
                    + Arrays.toString(a));
        MPI.Finalize();
    }
}

// Output for amount of processors equals 4 and n=4
//        myrank = 0 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
//        myrank = 3 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
//        myrank = 2 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
//        myrank = 1 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
