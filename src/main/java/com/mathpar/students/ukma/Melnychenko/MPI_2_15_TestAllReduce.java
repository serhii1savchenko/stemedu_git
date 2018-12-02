package com.mathpar.students.ukma.Bohachek;

import mpi.MPI;
import mpi.MPIException;

public class MPI_2_15_TestAllReduce {

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);


        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        int[] arr = new int[size];

        for (int i = 0; i < size; ++i)
        {
            arr[i] = i;
            System.out.println("rank = " + rank + "; arr[" + i + "] = " + arr[i]);
        }

        System.out.println();

        int[] res = new int[size];

        MPI.COMM_WORLD.allReduce(arr, res, size, MPI.INT, MPI.PROD);

        Thread.sleep(size * rank);

        System.out.println("\nProc #" + rank + " received:");

        for (int i = 0; i < res.length; i++)
            System.out.println(res[i]);

        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_2_15_TestAllReduce.java
mpirun java MPI_2_15_TestAllReduce

rank = 1; arr[0] = 0
rank = 1; arr[1] = 1
rank = 1; arr[2] = 2

rank = 2; arr[0] = 0rank = 0; arr[0] = 0
rank = 2; arr[1] = 1

rank = 0; arr[1] = 1rank = 2; arr[2] = 2


rank = 0; arr[2] = 2


Proc #0 received:
0
1
8

Proc #1 received:
0
1
8

Proc #2 received:
0
1
8
*/
