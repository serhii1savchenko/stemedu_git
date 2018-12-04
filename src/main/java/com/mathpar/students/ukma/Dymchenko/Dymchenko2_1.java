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
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko2_1 4
myrank = 0 : a = [0.8746523040916968, 0.15646273743886385, 0.271411239879486, 0.2778824756219639]
myrank = 1 : a = [0.8746523040916968, 0.15646273743886385, 0.271411239879486, 0.2778824756219639]

 */
