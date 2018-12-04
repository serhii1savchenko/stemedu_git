package com.mathpar.students.ukma.Dymchenko;

import mpi.MPI;
import mpi.MPIException;
import mpi.Intracomm;

public class Dymchenko9 {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
        int rank = WORLD.getRank();
        int procNum = WORLD.getSize();
        int[] arr = new int[procNum];

        if (rank == 0) {
            for (int i = 0; i < procNum; i++){
                arr[i] = i;
            }
        }
        int[] res = new int[procNum];
        WORLD.barrier();
        WORLD.scatter(arr, 1, MPI.INT, res, 1, MPI.INT, 0);
        System.out.println("Proc " + rank + " received:");
        for (int el : res)
            System.out.println(el);
        System.out.println();
        MPI.Finalize();
    }
}

/*
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko9
Proc 0 received:
1
0
Proc 1 received:
0
0
*/
