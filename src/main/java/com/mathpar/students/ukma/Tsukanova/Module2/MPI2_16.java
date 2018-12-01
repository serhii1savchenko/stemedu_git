package com.mathpar.students.ukma.Tsukanova.Module2;

import mpi.*;

public class MPI2_16 {
    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
        int rank = WORLD.getRank();
        int size = Integer.parseInt(args[0]);
        int arr[] = new int[size];
        for (int i = 0; i < size; ++i)
        {
            arr[i] = i;
            System.out.println("rank = " + rank + "; arr[" + i + "] = " + arr[i]);
        }
        System.out.println();
        int[] res = new int[size];
        WORLD.scan(arr, res, size, MPI.INT, MPI.SUM);
        Thread.sleep(size * rank);
        System.out.println("\nProc #" + rank + " received:");
        for (int el : res)
            System.out.println(el);
        MPI.Finalize();
    }
}

/*
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI2_16 4

RESULT
rank = 2; arr[0] = 0
rank = 2; arr[1] = 1
rank = 2; arr[2] = 2
rank = 2; arr[3] = 3

rank = 3; arr[0] = 0
rank = 3; arr[1] = 1
rank = 3; arr[2] = 2
rank = 3; arr[3] = 3

rank = 1; arr[0] = 0
rank = 1; arr[1] = 1
rank = 1; arr[2] = 2
rank = 1; arr[3] = 3

rank = 0; arr[0] = 0
rank = 0; arr[1] = 1
rank = 0; arr[2] = 2
rank = 0; arr[3] = 3


Proc #0 received:
Proc #1 received:
0
2
4
6

0
1
2
3

Proc #2 received:
0
3
6
9

Proc #3 received:
0
4
8
12
*/