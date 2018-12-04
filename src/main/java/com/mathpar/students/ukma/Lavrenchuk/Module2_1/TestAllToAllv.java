package com.mathpar.students.ukma.Lavrenchuk.Module2_1;

import mpi.MPI;
import mpi.MPIException;

public class TestAllToAllv {
    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);
        // Definition a processor amount
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
        // Completion a parallel part
        MPI.Finalize();
    }
}
// mpirun -np 2 java -cp /home/teacher/NetBeansProjects/JavaApplication1/build/classes TestAllReduce


//        OUTPUT:
//        myrank = 0; 0
//        myrank = 0; 1
//        myrank = 1; 2
//        myrank = 1; 3