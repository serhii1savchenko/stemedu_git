package com.mathpar.students.ukma.Snigur.Module2.Part_2;
import java.util.Arrays;
import mpi.*;

public class TestAllGather_3_6 {
    public static void main(String[] args)throws Exception {
        MPI.Init(args);
        Thread t = new Thread();
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for(int i = 0; i < n; i++) a[i] = myrank;
        System.out.println("myrank = " + myrank + " : a = "
                + Arrays.toString(a));
        int[] q = new int[n * np];
        MPI.COMM_WORLD.allGather(a, n, MPI.INT, q, n, MPI.INT);
        t.sleep(60 * myrank);
        System.out.println("myrank = " + myrank + " : q = "
                + Arrays.toString(q));
        MPI.Finalize();
    }
}
//mpirun java TestAllGather_3_6
// Output:
//        myrank = 0 : a = [0, 0, 0, 0]
//        myrank = 1 : a = [1, 1, 1, 1]
//        myrank = 0 : q = [0, 0, 0, 0, 1, 1, 1, 1]
//        myrank = 1 : q = [0, 0, 0, 0, 1, 1, 1, 1]