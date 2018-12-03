package com.mathpar.students.ukma.Fedorak.Module2; 

import mpi.MPI;
import mpi.MPIException;
public class MPI2_9 {
public static void main(String[] args) throws MPIException, InterruptedException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors and processor amount in a group
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        // Input an array size
        int n = 4;
        if (args.length != 0)
           n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        int[] q = new int[n * np];
        MPI.COMM_WORLD.allGatherv(a, n, MPI.INT, q, new int[]{n, n}, new int[]{0, 2}, MPI.INT);

            Thread.sleep(60 * myrank);
            System.out.println("myrank " + myrank + " : ");
            if (myrank == np - 1) {
                for (int i = 0; i < q.length; i++) {
                    System.out.println(" " + q[i]);
                }
            }
            System.out.println();

            MPI.Finalize();
        }
    
}
/*
mpirun -np 10 --hostfile hostfile java -cp class MPI2_2_HelloWorld

Proc num 1 Hello World
Proc num 2 Hello World
Proc num 8 Hello World
Proc num 3 Hello World
Proc num 0 Hello World
Proc num 6 Hello World
Proc num 9 Hello World
Proc num 5 Hello World
Proc num 4 Hello World
Proc num 7 Hello World

*/
