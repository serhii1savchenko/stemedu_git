package com.mathpar.students.ukma.Fedorak.Module2; 

import mpi.MPI;
import mpi.MPIException;
public class MPI2_17 {
    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors and processor amount in a group
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = 2;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        int[] q = new int[n];
        MPI.COMM_WORLD.scan(a, q, n, MPI.INT, MPI.SUM);
        for (int j = 0; j < np; j++) {
            if (myrank == j) {
                System.out.println("myrank = " + j);
                for (int i = 0; i < q.length; i++) {
                    System.out.println(" " + q[i]);
                }
            }
        }

        MPI.Finalize();
    }
}
/*
mpirun -np 4 --hostfile hostfile java -cp class com.mathpar.students.ukma.Fedorak.Module2.MPI2_17 

myrank = 0myrank = 1

 0 0
 1

 2
myrank = 3
myrank = 2 0

 4
 0
 3


*/
