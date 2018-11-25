package com.mathpar.students.ukma.Dymchenko;


import java.util.Random;
import mpi.*;

public class Dymchenko4 {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
        int rank = WORLD.getRank();
        int arrSize = Integer.parseInt(args[0]);
        double[] arr = new double[arrSize];
        if (rank == 0) {
            for (int i = 0; i < arrSize; i++) {
                arr[i] = new Random().nextDouble();
                System.out.println("a[" + i + "]= " + arr[i]);
            }
        }
        WORLD.barrier();
        WORLD.bcast(arr, arrSize, MPI.DOUBLE, 0);
        if (rank != 0) {
            for (int i = 0; i < arrSize; i++)
                System.out.println("a[" + i + "]= " + arr[i]);
        }
        MPI.Finalize();
    }
}

/*
Command: mpirun -np 3 java -cp out/production/Dymchenko4 Dymchenko4 5

Output:
a[0]= 0.8978978978978978
a[1]= 0.2342342342342342
a[2]= 0.6646462009829999
a[3]= 0.2223463475890092
a[4]= 0.66446352i5002359
a[0]= 0.8978978978978978
a[1]= 0.2342342342342342
a[2]= 0.6646462009829999
a[3]= 0.2223463475890092
a[4]= 0.66446352i5002359
a[0]= 0.8978978978978978
a[1]= 0.2342342342342342
a[2]= 0.6646462009829999
a[3]= 0.2223463475890092
a[4]= 0.66446352i5002359
*/
