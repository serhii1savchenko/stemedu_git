package com.mathpar.students.ukma.Tsukanova.Module2;

import java.util.Arrays;
import mpi.*;

public class MPI3_8 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int [] a = new int[n];
        for(int i = 0; i < n; i++) a[i] = myrank;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n];
        MPI.COMM_WORLD.allToAll(a, 1, MPI.INT, q, 1, MPI.INT);
        System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}

/*
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI3_8 4

RESULT
myrank = 2: a = [2, 2, 2, 2]
myrank = 3: a = [3, 3, 3, 3]
myrank = 1: a = [1, 1, 1, 1]
myrank = 0: a = [0, 0, 0, 0]
myrank = 1: q = [0, 1, 2, 3]
myrank = 3: q = [0, 1, 2, 3]
myrank = 2: q = [0, 1, 2, 3]
myrank = 0: q = [0, 1, 2, 3]

*/