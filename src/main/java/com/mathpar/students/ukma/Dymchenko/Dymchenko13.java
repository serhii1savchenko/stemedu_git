package com.mathpar.students.ukma.Dymchenko;


import mpi.MPI;
import mpi.MPIException;

import mpi.Intracomm;

public class Dymchenko13 {

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
        int rank = WORLD.getRank();
        int size = WORLD.getSize();
        int[] arr = new int[size];
        for (int i = 0; i < size; ++i)
        {
            arr[i] = i;
            System.out.println("rank = " + rank + "; arr[" + i + "] = " + arr[i]);
        }
        System.out.println();
        int[] res = new int[size];
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
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko13
rank = 0; arr[0] = 0
rank = 1; arr[0] = 0
rank = 1; arr[1] = 1rank = 0; arr[1] = 1



0
2

*/
