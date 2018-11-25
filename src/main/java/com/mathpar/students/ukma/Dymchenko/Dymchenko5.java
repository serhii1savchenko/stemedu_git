package com.mathpar.students.ukma.Dymchenko;


import mpi.*;

public class Dymchenko5 {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];
        int rank = WORLD.getRank();
        for(int i = 0; i < size; i++)
            arr[i] = rank;
        int procNum = WORLD.getSize();
        int[] res = new int[size * procNum];
        WORLD.gather(arr, size, MPI.INT, res, size, MPI.INT, procNum - 1);
        if(rank == procNum - 1) {
            for(int i = 0; i < res.length; i++)
                System.out.println(res[i]);
        }
        MPI.Finalize();
    }
}

/*
Command: mpirun -np 3 java -cp out/production/Dymchenko5 Dymchenko5 5

Output:
0
0
0
0
0
1
1
1
1
1
2
2
2
2
2
*/
