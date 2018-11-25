package com.mathpar.students.ukma.Bohachek;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class MPI_2_16_TestReduceScatter {

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

        int[] res = new int[1];

        int[] recvSizes = new int[size];
        Arrays.fill(recvSizes, 1);

        MPI.COMM_WORLD.reduceScatter(arr, res, recvSizes, MPI.INT, MPI.SUM);

        Thread.sleep(res.length * rank);

        System.out.println("\nProc #" + rank + " received:");

        for (int el : res)
            System.out.println(el);

        MPI.Finalize();
    }
}

/*
mpirun --hostfile /home/elizabeth/hostfile -np 3 java -cp /home/elizabeth/stemedu/target/classes com/mathpar/students/ukma/Bohachek/MPI_2_16_TestReduceScatter MPI_2_16_TestReduceScatter
rank = 0; arr[0] = 0
rank = 0; arr[1] = 1
rank = 0; arr[2] = 2

rank = 1; arr[0] = 0
rank = 2; arr[0] = 0
rank = 1; arr[1] = 1rank = 2; arr[1] = 1

rank = 2; arr[2] = 2
rank = 1; arr[2] = 2



Proc #0 received:
0

Proc #1 received:
3

Proc #2 received:
6

*/
