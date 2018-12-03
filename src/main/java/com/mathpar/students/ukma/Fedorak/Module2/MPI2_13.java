package com.mathpar.students.ukma.Fedorak.Module2; 

import mpi.MPI;
import mpi.MPIException;
public class MPI2_13 {
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
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.valueOf(i);
        }
        Integer[] b = new Integer[n];
        MPI.COMM_WORLD.allToAllv(a, new int[]{2, 2},
                new int[]{0, 2}, MPI.INT, b, new int[]{2, 2},
                new int[]{0, 2}, MPI.INT);
        for (int i = 0; i < 2; i++) {
            System.out.print("myrank = " + myrank
                    + "; " + b[i] + "\n");
        }

        MPI.Finalize();
    }
}
/*
mpirun -np 4 --hostfile hostfile java -cp class MPI2_13 4

 myrank = 0; 0
 myrank = 0; 1
 myrank = 1; 2
 myrank = 1; 3

*/
