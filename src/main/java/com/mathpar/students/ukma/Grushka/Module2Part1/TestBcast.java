package com.mathpar.students.ukma.Grushka.Module2Part1;

import java.util.Random;
import mpi.*;

/**
 * The processor with the number of 0 is sending an array of numbers
 * to other processors,
 * using distribution on a binary tree.
 */
public class TestBcast {
    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors
        int myrank = MPI.COMM_WORLD.getRank();
        // Input parameter - an array size
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        double[] a = new double[n];
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        MPI.COMM_WORLD.barrier();
        // Sending a data from processor with the number of 0 to others
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0) {
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }

        MPI.Finalize();
    }
}


//mpirun -np 3 java -cp out/production/Module2Part1 TestBcast

//  Result (3 processors & n=4):

//a[0]= 0.6705490722783738
//a[1]= 0.5616758975076603
//a[2]= 0.9248096778911326
//a[3]= 0.7386952779176705
//a[0]= 0.6705490722783738
//a[0]= 0.6705490722783738
//a[1]= 0.5616758975076603
//a[2]= 0.9248096778911326
//a[1]= 0.5616758975076603
//a[2]= 0.9248096778911326
//a[3]= 0.7386952779176705
//a[3]= 0.7386952779176705