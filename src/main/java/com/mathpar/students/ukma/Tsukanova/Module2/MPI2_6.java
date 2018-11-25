package com.mathpar.students.ukma.Tsukanova;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class MPI2_6 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        var WORLD = MPI.COMM_WORLD;
        int rank = WORLD.getRank();
        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];
        Arrays.fill(arr, rank);
        int procNum = WORLD.getSize();
        int[] res = new int[size * procNum];
        // How many elements to receive from each proc
        var recvCount = new int[procNum];
        Arrays.fill(recvCount, size);
        var offsets = new int[]{0, 2, 5};
        WORLD.gatherv(
            arr, size, MPI.INT, res, recvCount, offsets, MPI.INT, procNum - 1
        );
        if(rank == procNum - 1) {
            for(int i = 0; i < res.length; i++)
                System.out.println(res[i]);
        }
        MPI.Finalize();
    }
}

/*
COMMAND
 /home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 3 java -cp out/production/HelloWorld MPI2_6 4

RESULT
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
