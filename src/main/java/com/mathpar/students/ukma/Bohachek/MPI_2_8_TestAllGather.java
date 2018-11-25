package com.mathpar.students.ukma.Bohachek;
import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class MPI_2_8_TestAllGather {

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();

        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];
        Arrays.fill(arr, rank);

        int procNum = MPI.COMM_WORLD.getSize();
        int[] res = new int[size * procNum];

        MPI.COMM_WORLD.allGather(arr, size, MPI.INT, res, size, MPI.INT);

        Thread.sleep(size * rank);

        System.out.println("Proc " + rank + " received:");

        for (int el : res)
            System.out.println(el);

        System.out.println();

        MPI.Finalize();
    }
}

/*
mpirun --hostfile /home/elizabeth/hostfile -np 3 java -cp /home/elizabeth/stemedu/target/classes com.mathpar.students.ukma.Bohachek/MPI_2_8_TestAllGather 3
Proc 0 received:
0
0
0
1
1
1
2
2
2

Proc 1 received:
0
0
0
1
1
1
2
2
2

Proc 2 received:
0
0
0
1
1
1
2
2
2
*/
