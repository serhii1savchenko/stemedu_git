package com.mathpar.students.ukma.Tsukanova.Module2;

import java.util.Arrays;
import java.util.Random;
import mpi.*;


public class MPI3_1 {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
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


/*
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI3_1 4

RESULT
myrank = 0 : a = [0.1517159300125156, 0.44195586363975636, 0.833090732647228, 0.23123461414530855]
myrank = 2 : a = [0.1517159300125156, 0.44195586363975636, 0.833090732647228, 0.23123461414530855]
myrank = 1 : a = [0.1517159300125156, 0.44195586363975636, 0.833090732647228, 0.23123461414530855]
myrank = 3 : a = [0.1517159300125156, 0.44195586363975636, 0.833090732647228, 0.23123461414530855]
 */
