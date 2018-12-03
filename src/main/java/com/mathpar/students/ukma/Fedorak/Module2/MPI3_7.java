package com.mathpar.students.ukma.Fedorak.Module2;
import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class MPI3_7 {
    public static void main(String[] args)
            throws MPIException, InterruptedException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n*np];
        MPI.COMM_WORLD.allGatherv(a, n, MPI.INT,
                q, new int[]{n, n}, new int[]{2, 0}, MPI.INT);
        Thread.sleep(60 * myrank);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}

/*
mpirun -np 4 --hostfile hostfile java -cp class com.mathpar.students.ukma.Fedorak.Module2.MPI3_7 10 

myrank = 1: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
myrank = 0: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
myrank = 3: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
myrank = 2: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

*/
