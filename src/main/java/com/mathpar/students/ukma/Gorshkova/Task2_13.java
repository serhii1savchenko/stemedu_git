package com.mathpar.students.ukma.Gorshkova;
import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class Task2_13 {

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
Command: mpirun -np 3 java -cp out/production/2_13 2_13

Output:
rank = 2; arr[0] = 0
rank = 2; arr[1] = 1
rank = 2; arr[2] = 2

rank = 0; arr[0] = 0
rank = 0; arr[1] = 1
rank = 0; arr[2] = 2
rank = 1; arr[0] = 0

rank = 1; arr[1] = 1
rank = 1; arr[2] = 2

0
3
6
*/
