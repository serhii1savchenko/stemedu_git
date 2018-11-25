package com.mathpar.students.ukma.Dymchenko;

import java.util.Arrays;
import java.util.Random;
import mpi.*;

public class Dymchenko2_1 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        double[] a = new double[n];
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
            }

            System.out.println("myrank = " + myrank + " : a = " + Arrays.toString(a));
        }
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0)
            System.out.println("myrank = " + myrank + " : a = " + Arrays.toString(a));
        MPI.Finalize();
    }
}

/*
Command: mpirun -np 4 java -cp out/production/Dymchenko2_1 Dymchenko2_1 4

Output:
myrank = 0 : a = [0.3402823488867737, 0.91992036178123891, 0.003929196202030, 0.39881281239124012]
myrank = 2 : a = [0.3402823488867737, 0.91992036178123891, 0.003929196202030, 0.39881281239124012]
myrank = 1 : a = [0.3402823488867737, 0.91992036178123891, 0.003929196202030, 0.39881281239124012]
myrank = 3 : a = [0.3402823488867737, 0.91992036178123891, 0.003929196202030, 0.39881281239124012]
 */
