package com.mathpar.students.ukma.Bohachek;

import mpi.MPI;
import mpi.MPIException;

public class MPI_2_11_TestScatterv {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();

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

        int[] sendSizes = new int[]{3, 2, 1, 1}; // depends on proc num
        int[] offsets = new int[]{0, 1, 2, 0};   // depends on proc num
       
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatterv(arr, sendSizes, offsets, MPI.INT, res, size, MPI.INT, 0);

        System.out.println("Proc " + rank + " received:");

        for (int el : res)
            System.out.println(el);

        System.out.println();

        MPI.Finalize();
    }
}

/*
mpirun --hostfile /home/elizabeth/hostfile -np 2 java -cp /home/elizabeth/stemedu/target/classes com/mathpar/students/ukma/Bohachek/MPI_2_11_TestScatterv 4
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
*/