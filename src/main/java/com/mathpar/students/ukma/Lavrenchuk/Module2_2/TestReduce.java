package com.mathpar.students.ukma.Lavrenchuk.Module2_2;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class TestReduce {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n];
        MPI.COMM_WORLD.reduce(a, q, n, MPI.INT, MPI.SUM, 0);
        if (myrank == 0)
            System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}
// mpirun -np 2 java -cp /home/teacher/NetBeansProjects/JavaApplication1/build/classes TestReduce

//        OUTPUT:
//        myrank = 0: a = [0, 1, 2, 3]
//        myrank = 1: a = [0, 1, 2, 3]
//        myrank = 2: a = [0, 1, 2, 3]
//        myrank = 3: a = [0, 1, 2, 3]
//        myrank = 0: q = [0, 4, 8, 12]
