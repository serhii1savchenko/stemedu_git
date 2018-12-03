package com.mathpar.students.ukma.Fedorak.Module2;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class MPI3_3 {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
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

/*
mpirun -np 4 --hostfile hostfile java -cp class com.mathpar.students.ukma.Fedorak.Module2.MPI3_3 10 


myrank = 3 : a = [3, 3, 3, 3]
myrank = 2 : a = [2, 2, 2, 2]
myrank = 0 : a = [0, 0, 0, 0]
myrank = 1 : a = [1, 1, 1, 1]
myrank = 3 : q = [0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3]
*/
