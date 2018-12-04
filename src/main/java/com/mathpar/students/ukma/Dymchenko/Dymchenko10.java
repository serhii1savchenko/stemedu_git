package com.mathpar.students.ukma.Dymchenko;

import mpi.MPI;
import mpi.MPIException;

public class Dymchenko10 {

    
public static void main(String[] args) throws MPIException {
            MPI.Init(args);
            int myrank = MPI.COMM_WORLD.getRank();
            int n = 4;
            if (args.length != 0)
            {
                n = Integer.parseInt(args[0]);
            }
            int[] a = new int[n];
            if (myrank == 0) {
                for (int i = 0; i < a.length; i++)
                    a[i] = i;
            }
            int[] q = new int[n];
            MPI.COMM_WORLD.barrier();
            MPI.COMM_WORLD.scatterv(a, new int[]{3, 2, 1, 1},
                    new int[]{0, 1, 2, 0}, MPI.INT, q, n, MPI.INT, 0);
            for (int i = 0; i < q.length; i++)
                System.out.print("myrank = " + myrank
                        + "; " + q[i] + "\n");
            MPI.Finalize();
        }
}

/*

public static void main(String[] args) throws MPIException {
            MPI.Init(args);
            int myrank = MPI.COMM_WORLD.getRank();
            int n = 4;
            if (args.length != 0)
            {
                n = Integer.parseInt(args[0]);
            }
            int[] a = new int[n];
            if (myrank == 0) {
                for (int i = 0; i < a.length; i++)
                    a[i] = i;
            }
            int[] q = new int[n];
            MPI.COMM_WORLD.barrier();
            MPI.COMM_WORLD.scatterv(a, new int[]{3, 2, 1, 1},
                    new int[]{0, 1, 2, 0}, MPI.INT, q, n, MPI.INT, 0);
            for (int i = 0; i < q.length; i++)
                System.out.print("myrank = " + myrank
                        + "; " + q[i] + "\n");
            MPI.Finalize();
        }


*/
