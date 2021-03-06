package com.mathpar.students.ukma.Grushka.Module2Part2;

import mpi.*;

import java.util.Arrays;

public class TestScatter {
    public static void main(String[] args)
            throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = 4;
        if (args.length != 0)
            n = Integer.parseInt(args[0]);
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


//mpirun -np 4 java -cp out/production/Module2Part2 TestScatter

// Result (4 processors):
// myrank = 0: a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
// myrank = 0: q = [0, 1, 2, 3]
// myrank = 2: q = [8, 9, 10, 11]
// myrank = 1: q = [4, 5, 6, 7]
// myrank = 3: q = [12, 13, 14, 15]