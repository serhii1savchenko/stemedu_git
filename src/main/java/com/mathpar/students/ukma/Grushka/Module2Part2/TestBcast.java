package com.mathpar.students.ukma.Grushka.Module2Part2;

import java.util.Arrays;
import java.util.Random;
import mpi.*;

/**
 * The processor with the number of 0 is sending an array of numbers
 * to other processors
 * of communicator MPI.COMM_WORLD
 */
public class TestBcast {
    public static void main(String[] args)
            throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors
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


//mpirun -np 3 java -cp out/production/Module2Part2 TestBcast

// Result (3 processors & n=4):
// myrank = 0 : a = [0.009396947561855273, 0.12708019486721656, 0.9817529107933417, 0.015285052110488806]
// myrank = 2 : a = [0.009396947561855273, 0.12708019486721656, 0.9817529107933417, 0.015285052110488806]
// myrank = 1 : a = [0.009396947561855273, 0.12708019486721656, 0.9817529107933417, 0.015285052110488806]