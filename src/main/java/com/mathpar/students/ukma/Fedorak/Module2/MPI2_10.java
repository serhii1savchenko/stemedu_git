package com.mathpar.students.ukma.Fedorak.Module2; 

import mpi.MPI;
import mpi.MPIException;
public class MPI2_10 {
public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors
        int myrank = MPI.COMM_WORLD.getRank();
        // Declaration an array of objects
        int[] a = new int[2];
        // Filling these array on a first processor
        if (myrank == 0) {
            for (int i = 0; i < 2; i++) {
                a[i] = i;
                System.out.println("a = " + a[i] + " ");
            }
            System.out.println("rank = " + myrank);
        }
        // Declaration an array in which elements accepted by the processor
        // will be recorded
        int[] q = new int[2];
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatter(a, 1, MPI.INT, q, 1, MPI.INT, 0);
        // Printing received arrays and numbers of processors
        for (int i = 0; i < q.length; i++)
            System.out.println("myrank = " + myrank + " ; " + q[i] + "\n");

        MPI.Finalize();
    }
}
/*
mpirun -np 4 --hostfile hostfile java -cp class MPI2_10 4


a = 0 
a = 1 
rank = 0
myrank = 1 ; 22423881
myrank = 3 ; 1970230132

myrank = 1 ; 0

myrank = 3 ; 0

myrank = 2 ; 1701251328

myrank = 2 ; 0


*/
