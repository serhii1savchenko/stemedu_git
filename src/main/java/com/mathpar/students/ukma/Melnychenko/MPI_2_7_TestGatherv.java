package com.mathpar.students.ukma.Bohachek;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class MPI_2_7_TestGatherv {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();

        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];
        Arrays.fill(arr, rank);

        int procNum = MPI.COMM_WORLD.getSize();
        int[] res = new int[size * procNum];

        // How many elements to receive from each proc
        int[] recvCount = new int[procNum];
        Arrays.fill(recvCount, size);

        int[] offsets = new int[]{0, 2, 5};

        MPI.COMM_WORLD.gatherv(
            arr, size, MPI.INT, res, recvCount, offsets, MPI.INT, procNum - 1
        );

        if(rank == procNum - 1) {
            for(int i = 0; i < res.length; i++)
                System.out.println(res[i]);
        }

        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_2_7_TestGatherv.java
mpirun java MPI_2_7_TestGatherv 3

0
0
1
1
1
2
2
2
0

*/
