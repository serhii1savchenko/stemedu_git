package com.mathpar.students.ukma.Gorshkova;

import mpi.*;

public class Task2_16 {

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);

        var WORLD = MPI.COMM_WORLD;

        var rank = WORLD.getRank();
        var size = WORLD.getSize();

        var arr = new int[size];

        for (int i = 0; i < size; ++i)
        {
            arr[i] = i;
            System.out.println("rank = " + rank + "; arr[" + i + "] = " + arr[i]);
        }

        System.out.println();

        var res = new int[size];

        WORLD.scan(arr, res, size, MPI.INT, MPI.SUM);

        Thread.sleep(size * rank);

        System.out.println("\nProc #" + rank + " received:");

        for (int el : res)
            System.out.println(el);

        MPI.Finalize();
    }
}

/*
Command: mpirun -np 3 java -cp out/production/2_16 2_16

Output:
rank = 0; arr[0] = 0
rank = 0; arr[1] = 1
rank = 0; arr[2] = 2

rank = 2; arr[0] = 0
rank = 2; arr[1] = 1
rank = 2; arr[2] = 2

rank = 1; arr[0] = 0
rank = 1; arr[1] = 1
rank = 1; arr[2] = 2

Proc #0 received:
0
1
2

Proc #1 received:
0
2
4

Proc #2 received:
0
3
6
*/