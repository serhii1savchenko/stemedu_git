package com.mathpar.students.ukma.lysenko;

import mpi.MPI;
import mpi.*;

public class Task_MPI_2_10 {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        Intracomm WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();

        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];

        if (rank == 0) {
            for (int i = 0; i < size; i++){
                arr[i] = i;
                System.out.println("arr[" + i + "] = " + arr[i]);
            }

            System.out.println();
        }

        int[] res = new int[size];

        int[] sendSizes = new int[]{3, 2, 1, 1};
        int[] offsets = new int[]{0, 1, 2, 0};

        WORLD.scatterv(arr, sendSizes, offsets, MPI.INT, res, size, MPI.INT, 0);
        WORLD.barrier();

        System.out.println("Proc " + rank + " received:");

        for (int el : res)
            System.out.println(el);

        System.out.println();

        MPI.Finalize();
    }
}

/*
Command: mpirun -np 4 java -cp out/production/MPI_2_10 MPI_2_10 4

Output:
arr[0] = 0
arr[1] = 1
arr[2] = 2
arr[3] = 3

Proc 0 received:
0
1
2
0

Proc 1 received:
1
2
0
0

Proc 2 received:
2
0
0
0

Proc 3 received:
0
0
0
0

*/
