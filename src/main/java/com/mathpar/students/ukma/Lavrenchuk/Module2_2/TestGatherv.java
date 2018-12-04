package com.mathpar.students.ukma.Lavrenchuk.Module2_2;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class TestGatherv {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = myrank;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n * np];
        MPI.COMM_WORLD.gatherv(a, n, MPI.INT, q, new int[]{n, n},
                new int[]{0, 2}, MPI.INT, np - 1);
        if(myrank == np-1)
            System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}
// mpirun -np 2 java -cp /home/teacher/NetBeansProjects/JavaApplication1/build/classes TestGatherv

//        OUTPUT:
//        myrank = 0: a = [0, 0, 0, 0]
//        myrank = 1: a = [1, 1, 1, 1]
//        myrank = 1: q = [0, 0, 1, 1, 1, 1, 0, 0]