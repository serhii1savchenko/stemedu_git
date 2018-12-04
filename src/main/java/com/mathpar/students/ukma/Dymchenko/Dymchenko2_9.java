package com.mathpar.students.ukma.Dymchenko;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class Dymchenko2_9 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();
        int[] a = new int[size];
        for (int i = 0; i < size; i++)
            a[i] = myrank;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[size];
        int[] sendSizes = new int[size];
        int[] recvSizes = new int[size];
        Arrays.fill(sendSizes, 1);
        Arrays.fill(recvSizes, 1);
        int[] sendOffsets = new int[size];
        int[] recvOffsets = new int[size];
        for (int i = 0; i < size; i++)
        {
            sendOffsets[i] = i;
            recvOffsets[i] = i;
        }
        MPI.COMM_WORLD.allToAllv(a, sendSizes, sendOffsets, MPI.INT, q, recvSizes, recvOffsets, MPI.INT);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}

/*
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko2_9 4
myrank = 0: a = [0, 0]
myrank = 1: a = [1, 1]
myrank = 1: q = [0, 1]
myrank = 0: q = [0, 1]

*/