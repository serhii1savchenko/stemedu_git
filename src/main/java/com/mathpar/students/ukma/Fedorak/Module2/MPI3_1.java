package com.mathpar.students.ukma.Fedorak.Module2;

import java.util.Arrays;
import java.math.BigInteger;
import java.util.Random;
import mpi.*;


public class MPI3_1 {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        double[] a = new double[n];
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
            }
            System.out.println("myrank = " + myrank + " : a = "
                    + Arrays.toString(a));
        }
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0)
            System.out.println("myrank = " + myrank + " : a = "
                    + Arrays.toString(a));
        MPI.Finalize();
    }
}


/*
mpirun -np 4 --hostfile hostfile java -cp class com.mathpar.students.ukma.Fedorak.Module2.MPI3_1 10 


myrank = 0 : a = [0.7776531629950051, 0.08053313396893402, 0.793630019804763, 0.6898142829290416, 0.41472818873892725, 0.4079695691653207, 0.4252488665915215, 0.7958662879429244, 0.6118678396013051, 0.17070164112574038]
myrank = 3 : a = [0.7776531629950051, 0.08053313396893402, 0.793630019804763, 0.6898142829290416, 0.41472818873892725, 0.4079695691653207, 0.4252488665915215, 0.7958662879429244, 0.6118678396013051, 0.17070164112574038]
myrank = 1 : a = [0.7776531629950051, 0.08053313396893402, 0.793630019804763, 0.6898142829290416, 0.41472818873892725, 0.4079695691653207, 0.4252488665915215, 0.7958662879429244, 0.6118678396013051, 0.17070164112574038]
myrank = 2 : a = [0.7776531629950051, 0.08053313396893402, 0.793630019804763, 0.6898142829290416, 0.41472818873892725, 0.4079695691653207, 0.4252488665915215, 0.7958662879429244, 0.6118678396013051, 0.17070164112574038]

 */
