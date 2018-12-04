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
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko5 5
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

*/
