package com.mathpar.students.ukma.Tsukanova;

import java.math.BigInteger;
import java.util.Random;
import mpi.*;

public class MPI2_4 {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        var WORLD = MPI.COMM_WORLD;
        int rank = WORLD.getRank();
        int arrSize = Integer.parseInt(args[0]);
        double[] arr = new double[arrSize];
        if (rank == 0) {
            for (int i = 0; i < arrSize; i++) {
                arr[i] = new Random().nextDouble();
                System.out.println("a[" + i + "]= " + arr[i]);
            }
        }
        WORLD.barrier();
        WORLD.bcast(arr, arrSize, MPI.DOUBLE, 0);
        if (rank != 0) {
            for (int i = 0; i < arrSize; i++)
                System.out.println("a[" + i + "]= " + arr[i]);
        }
        MPI.Finalize();
    }
}

/*
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI2_4 2

RESULT
a[0]= 0.6474799698832825
a[1]= 0.8044581411778884
a[0]= 0.6474799698832825
a[1]= 0.8044581411778884
a[0]= 0.6474799698832825
a[1]= 0.8044581411778884
a[0]= 0.6474799698832825
a[1]= 0.8044581411778884
*/
