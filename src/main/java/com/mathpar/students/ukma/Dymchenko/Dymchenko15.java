package com.mathpar.students.ukma.Dymchenko;


import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import mpi.Intracomm;

public class Dymchenko15 {

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
        int[] res = new int[1];
        int[] recvSizes = new int[size];
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
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko15
rank = 0; arr[0] = 0
rank = 0; arr[1] = 1

rank = 1; arr[0] = 0
rank = 1; arr[1] = 1


Proc #0 received:
0

Proc #1 received:
2

*/
