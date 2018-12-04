package com.mathpar.students.ukma.Dymchenko;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import mpi.Intracomm;

public class Dymchenko12 {

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
        int rank = WORLD.getRank();
        int size = WORLD.getSize();
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; ++i)
        {
            arr[i] = i;
            System.out.println("rank = " + rank + "; arr[" + i + "] = " + arr[i]);
        }
        System.out.println();
        Integer[] res = new Integer[size];
        int[] sendSizes = new int[size];
        int[] recvSizes = new int[size];
        Arrays.fill(sendSizes, 1);
        Arrays.fill(recvSizes, 1);
        int[] sendOffsets = new int[size];
        int[] recvOffsets = new int[size];
        for (int i = 0; i < size; i++)
        {
            sendOffsets[i] = i;
            recvOffsets[i] = i;
        }
        WORLD.allToAllv(arr, sendSizes, sendOffsets, MPI.INT, res,recvSizes, recvOffsets, MPI.INT);
        Thread.sleep(size * rank);
        for (int i = 0; i < size; i++)
            System.out.println("rank = " + rank + "; send = " + arr[i] + "; recv = " + res[i]);

        System.out.println();
        MPI.Finalize();
    }
}

/*
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko12
rank = 0; arr[0] = 0
rank = 0; arr[1] = 1

rank = 1; arr[0] = 0
rank = 1; arr[1] = 1

rank = 0; send = 0; recv = 0
rank = 0; send = 1; recv = 0

rank = 1; send = 0; recv = 1
rank = 1; send = 1; recv = 1

*/
