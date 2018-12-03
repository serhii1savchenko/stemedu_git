package com.mathpar.students.ukma.Fedorak.Module2;

import java.util.Arrays;
import mpi.*;

public class MPI3_4 {
    public static void main(String[] args)
            throws MPIException{
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n*np];
        if (myrank == 0){
            for (int i = 0; i < a.length; i++)
                a[i] = i ;
            System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        }
        int[] q = new int[n];
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatter(a,n, MPI.INT, q,n, MPI.INT, 0);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}

/*
mpirun -np 4 --hostfile hostfile java -cp class com.mathpar.students.ukma.Fedorak.Module2.MPI3_4 10 

myrank = 0: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39]
myrank = 0: q = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
myrank = 2: q = [20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
myrank = 3: q = [30, 31, 32, 33, 34, 35, 36, 37, 38, 39]
myrank = 1: q = [10, 11, 12, 13, 14, 15, 16, 17, 18, 19]


*/
