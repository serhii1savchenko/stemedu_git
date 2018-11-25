package com.mathpar.students.ukma.Tsukanova;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class MPI2_13 {
    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);
        var WORLD = MPI.COMM_WORLD;
        var rank = WORLD.getRank();
        var size = WORLD.getSize();
        var arr = new int[size];
        for (int i = 0; i < size; ++i)
        {
            arr[i] = i;
            System.out.println("rank = " + rank + "; arr[" + i + "] = " + arr[i]);
        }
        System.out.println();
        var res = new int[size];
        WORLD.reduce(arr, res, size, MPI.INT, MPI.SUM, 0);
        Thread.sleep(size * rank);
        if (rank == 0) {
            for (int i = 0; i < res.length; i++)
                System.out.println(res[i]);
        }
        MPI.Finalize();
    }
}

/*
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI2_13 4

RESULT
rank = 3; arr[0] = 0
rank = 3; arr[1] = 1
rank = 3; arr[2] = 2
rank = 3; arr[3] = 3

rank = 2; arr[0] = 0
rank = 2; arr[1] = 1
rank = 2; arr[2] = 2
rank = 2; arr[3] = 3

rank = 0; arr[0] = 0
rank = 0; arr[1] = 1
rank = 0; arr[2] = 2
rank = 0; arr[3] = 3

rank = 1; arr[0] = 0
rank = 1; arr[1] = 1
rank = 1; arr[2] = 2
rank = 1; arr[3] = 3

0
4
8
12
*/
