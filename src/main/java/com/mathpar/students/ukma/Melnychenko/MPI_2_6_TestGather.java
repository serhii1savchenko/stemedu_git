package com.mathpar.students.ukma.Bohachek;

import mpi.*;

public class MPI_2_6_TestGather {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];

        int rank = MPI.COMM_WORLD.getRank();

        for(int i = 0; i < size; i++)
            arr[i] = rank;

        int procNum = MPI.COMM_WORLD.getSize();
        int[] res = new int[size * procNum];

        MPI.COMM_WORLD.gather(arr, size, MPI.INT, res, size, MPI.INT, procNum - 1);

        if(rank == procNum - 1) {
            for(int i = 0; i < res.length; i++)
                System.out.println(res[i]);
        }

        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_2_6_TestGather.java
mpirun java MPI_2_6_TestGather 4

0
0
0
0
1
1
1
1
2
2
2
2
3
3
3
3

*/
