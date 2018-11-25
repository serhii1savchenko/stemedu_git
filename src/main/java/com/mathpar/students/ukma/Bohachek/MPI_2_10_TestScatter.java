package com.mathpar.students.ukma.Bohachek;

import mpi.MPI;
import mpi.MPIException;

public class MPI_2_10_TestScatter {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();

        int procNum = MPI.COMM_WORLD.getSize();
        int[] arr = new int[procNum];

        if (rank == 0) {
            for (int i = 0; i < procNum; i++){
                arr[i] = i;
                System.out.println("arr[" + i + "] = " + arr[i]);
            }

            System.out.println();
        }

        int[] res = new int[procNum];

        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatter(arr, 1, MPI.INT, res, 1, MPI.INT, 0);

        System.out.println("Proc " + rank + " received:");

        for (int el : res)
            System.out.println(el);

        System.out.println();

        MPI.Finalize();
    }
}

/*

mpirun --hostfile /home/elizabeth/hostfile -np 2 java -cp /home/elizabeth/stemedu/target/classes com/mathpar/students/ukma/Bohachek/MPI_2_10_TestScatter
arr[0] = 0
arr[1] = 1

Proc 0 received:
0
0

Proc 1 received:
1
0


*/
