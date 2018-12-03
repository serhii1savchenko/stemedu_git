package com.mathpar.students.ukma.Fedorak.Module2; 

import mpi.MPI;
import mpi.MPIException;
public class MPI2_11 {
        public static void main(String[] args) throws MPIException {
            // MPI definition
            MPI.Init(args);
            // Determine the number of processors
            int myrank = MPI.COMM_WORLD.getRank();
            int n = 4;
            if (args.length != 0)
                n = Integer.parseInt(args[0]);
            // Declaration an array of objects
            int[] a = new int[n];
            // Filling these array on a first processor
            if (myrank == 0) {
                for (int i = 0; i < a.length; i++)
                    a[i] = i;
            }
            // Declaration an array in which elements accepted by the processor
            // will be recorded
            int[] q = new int[n];
            MPI.COMM_WORLD.barrier();
            MPI.COMM_WORLD.scatterv(a, new int[]{3, 2, 1, 1},
                    new int[]{0, 1, 2, 0}, MPI.INT, q, n, MPI.INT, 0);
            // Printing received arrays and numbers of processors which has accepted
            for (int i = 0; i < q.length; i++)
                System.out.print("myrank = " + myrank
                        + "; " + q[i] + "\n");

            MPI.Finalize();
        }
}
/*
mpirun -np 4 --hostfile hostfile java -cp class MPI2_11 4


myrank = 2; 2
myrank = 0; 0
myrank = 0; 1
myrank = 0; 2
myrank = 0; 0
myrank = 1; 1
myrank = 1; 2
myrank = 1; 0
myrank = 3; 0
myrank = 1; 0
myrank = 2; 0
myrank = 2; 0
myrank = 2; 0
myrank = 3; 0
myrank = 3; 0
myrank = 3; 0


*/
