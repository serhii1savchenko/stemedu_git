package com.mathpar.students.ukma.Tsukanova.Module2;

import java.util.Arrays;
import mpi.*;

public class MPI3_2 {
    public static void main(String[] args)
            throws Exception{
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for(int i = 0; i < n; i++) a[i] = myrank;
        System.out.println("myrank = " + myrank + " : a = "
                + Arrays.toString(a));
        int[] q = new int[n*np];
        MPI.COMM_WORLD.gather(a, n, MPI.INT, q, n, MPI.INT,
                np-1);
        if(myrank == np-1)
            System.out.println("myrank = " + myrank + " : q = "
                    + Arrays.toString(q));
        MPI.Finalize();
    }
}


/*
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI3_2 4

RESULT
myrank = 0 : a = [0.1517159300125156, 0.44195586363975636, 0.833090732647228, 0.23123461414530855]
myrank = 2 : a = [0.1517159300125156, 0.44195586363975636, 0.833090732647228, 0.23123461414530855]
myrank = 1 : a = [0.1517159300125156, 0.44195586363975636, 0.833090732647228, 0.23123461414530855]
myrank = 3 : a = [0.1517159300125156, 0.44195586363975636, 0.833090732647228, 0.23123461414530855]
 */
