package com.mathpar.students.ukma.Dymchenko;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class Dymchenko10 {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
        int rank = WORLD.getRank();
        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];
        if (rank == 0) {
            for (int i = 0; i < size; i++){
                arr[i] = i;
            }
        }
        int[] res = new int[size];
        WORLD.scatterv(arr, new int[]{3, 2, 1, 1}, new int[]{0, 3, 2, 0}, MPI.INT, res, size, MPI.INT, 0);
        WORLD.barrier();
        System.out.println();
        System.out.println("Proc " + rank + " received:");
        for (int el : res)
            System.out.println(el);
        System.out.println();
        MPI.Finalize();
    }
}

/*
Command: mpirun -np 4 java -cp out/production/Dymchenko10 Dymchenko10 4

Output:

Proc 0 received:
0
3
2
0

Proc 1 received:
3
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
