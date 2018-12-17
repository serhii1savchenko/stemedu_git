package com.mathpar.students.ukma.Zadorozhnyi.Module2.Part_2;
import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class TestAllGatherv_3_7 {
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
                q, new int[]{n, n}, new int[]{0, 2}, MPI.INT);
        Thread.sleep(60 * myrank);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}
//mpirun java TestAllGatherv_3_7
// Output:
//        myrank = 0: a = [0, 1]
//        myrank = 1: a = [0, 1]
//        myrank = 0: q = [0, 1, 0, 1]
//        myrank = 1: q = [0, 1, 0, 1]


