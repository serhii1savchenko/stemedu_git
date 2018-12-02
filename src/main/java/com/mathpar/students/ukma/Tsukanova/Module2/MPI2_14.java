package com.mathpar.students.ukma.Tsukanova.Module2;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class MPI2_14 {
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
        WORLD.allReduce(arr, res, size, MPI.INT, MPI.PROD);
        Thread.sleep(size * rank);
        System.out.println("\nProc #" + rank + " received:");
        for (int i = 0; i < res.length; i++)
            System.out.println(res[i]);
        MPI.Finalize();
    }
}

/*
COMMAND

/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI2_14 4
rank = 1; arr[0] = 0
rank = 1; arr[1] = 1
rank = 1; arr[2] = 2
rank = 1; arr[3] = 3

rank = 3; arr[0] = 0
rank = 3; arr[1] = 1
rank = 3; arr[2] = 2
rank = 3; arr[3] = 3

rank = 0; arr[0] = 0
rank = 0; arr[1] = 1
rank = 0; arr[2] = 2
rank = 0; arr[3] = 3

rank = 2; arr[0] = 0
rank = 2; arr[1] = 1
rank = 2; arr[2] = 2
rank = 2; arr[3] = 3


Proc #0 received:
0
1
16
81

Proc #1 received:
0
1
16
81

Proc #2 received:
0
1
16
81

Proc #3 received:
0
1
16
81
*/

