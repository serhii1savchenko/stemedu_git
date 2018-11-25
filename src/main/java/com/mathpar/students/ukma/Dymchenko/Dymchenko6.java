package com.mathpar.students.ukma.Dymchenko;


import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import mpi.Intracomm;

public class Dymchenko6 {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
        int rank = WORLD.getRank();
        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];
        Arrays.fill(arr, rank);
        int procNum = WORLD.getSize();
        int[] res = new int[size * procNum];
        int[] recvCount = new int[procNum];
        Arrays.fill(recvCount, size);

        int[] offsets = new int[]{0, 2, 5};

        WORLD.gatherv(arr, size, MPI.INT, res, recvCount, offsets, MPI.INT, procNum - 1);
        if(rank == procNum - 1) {
            for(int i = 0; i < res.length; i++)
                System.out.println(res[i]);
        }
        MPI.Finalize();
    }
}

/*
Command: mpirun -np 3 java -cp out/production/Dymchenko6 Dymchenko6 4

Output:
0
0
1
1
1
2
2
2
2
0
0
0
*/
