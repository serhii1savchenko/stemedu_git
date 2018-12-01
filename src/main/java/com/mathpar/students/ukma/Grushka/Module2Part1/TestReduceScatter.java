package com.mathpar.students.ukma.Grushka.Module2Part1;

import mpi.MPI;
import mpi.MPIException;

public class TestReduceScatter {
    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors
        int myrank = MPI.COMM_WORLD.getRank();
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        int[] q = new int[n];
        MPI.COMM_WORLD.reduceScatter(a, q, new int[]{2, 2},
                MPI.INT, MPI.SUM);
        if (myrank == 0) {
            for (int i = 0; i < q.length; i++) {
                System.out.println(" " + q[i]);
            }
        }

        MPI.Finalize();
    }
}


//mpirun -np 2 java -cp out/production/Module2Part1 TestReduceScatter

//  Result (2 processors & n=4):
//         0
//         2
//         0
//         0
