package com.mathpar.students.ukma.Kladko;


import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class MPI_2_15 {

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

        var res = new int[1];

        var recvSizes = new int[size];
        Arrays.fill(recvSizes, 1);

        WORLD.reduceScatter(arr, res, recvSizes, MPI.INT, MPI.SUM);

        Thread.sleep(res.length * rank);

        System.out.println("\nProc #" + rank + " received:");

        for (int el : res)
            System.out.println(el);

        MPI.Finalize();
    }
}

/*
Command: mpirun -np 3 java -cp out/production/MPI_2_15 MPI_2_15

Output:
rank = 0; arr[0] = 0
rank = 0; arr[1] = 1
rank = 0; arr[2] = 2

rank = 2; arr[0] = 0
rank = 2; arr[1] = 1
rank = 2; arr[2] = 2

rank = 1; arr[0] = 0
rank = 1; arr[1] = 1
rank = 1; arr[2] = 2


Proc #0 received:
0

Proc #1 received:
3

Proc #2 received:
6
*/
