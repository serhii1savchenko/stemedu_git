package com.mathpar.students.ukma.Grushka.Module2Part2;

import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;

public class TestAllReduce {
    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = 2;
        if (args.length != 0)
            n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n];
        MPI.COMM_WORLD.allReduce(a, q, n, MPI.INT, MPI.PROD);
        for (int j = 0; j < np; j++)
            if (myrank == j) {
                System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
            }
        MPI.Finalize();
    }
}


//mpirun -np 4 java -cp out/production/Module2Part2 TestAllReduce

// Result (4 processors & n=2):
// myrank = 0: a = [0, 1]
// myrank = 2: a = [0, 1]
// myrank = 1: a = [0, 1]
// myrank = 1: q = [0, 1]
// myrank = 2: q = [0, 1]
// myrank = 0: q = [0, 1]