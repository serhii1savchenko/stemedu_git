package com.mathpar.students.ukma.Tsukanova;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class MPI2_12 {
    public static void main(String[] args) throws MPIException, InterruptedException {
       MPI.Init(args);
        var WORLD = MPI.COMM_WORLD;
        var rank = WORLD.getRank();
        var size = WORLD.getSize();
        var arr = new Integer[size];
        for (int i = 0; i < size; ++i)
        {
            arr[i] = i;
            System.out.println("rank = " + rank + "; arr[" + i + "] = " + arr[i]);
        }
        System.out.println();
        var res = new Integer[size];
        var sendSizes = new int[size];
        var recvSizes = new int[size];
        Arrays.fill(sendSizes, 1);
        Arrays.fill(recvSizes, 1);
        var sendOffsets = new int[size];
        var recvOffsets = new int[size];
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
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 2 java -cp out/production/HelloWorld MPI2_12 4

RESULT
rank = 1; arr[0] = 0rank = 0; arr[0] = 0
rank = 1; arr[1] = 1


rank = 0; arr[1] = 1

rank = 0; send = 0; recv = 0
rank = 0; send = 1; recv = 0

rank = 1; send = 0; recv = 1
rank = 1; send = 1; recv = 1
*/
