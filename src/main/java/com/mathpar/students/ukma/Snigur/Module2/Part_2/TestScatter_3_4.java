package com.mathpar.students.ukma.Snigur.Module2.Part_2;
import java.util.Arrays;
import mpi.*;

public class TestScatter_3_4 {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n * np];
        if (myrank == 0) {
            for (int i = 0; i < a.length; i++)
                a[i] = i;
            System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        }
        int[] q = new int[n];
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatter(a, n, MPI.INT, q, n, MPI.INT, 0);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}
//mpirun java TestScatter_3_4
// Output:
//        myrank = 0: a = [0, 1, 2, 3, 4, 5, 6, 7]
//        myrank = 0: q = [0, 1, 2, 3]
//        myrank = 1: q = [4, 5, 6, 7]
