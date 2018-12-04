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
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko4 5
a[0]= 0.6851531806684136
a[1]= 0.27026242360103137
a[2]= 0.6705225084244828
a[3]= 0.8994444352553269
a[4]= 0.9389234912589952
a[0]= 0.6851531806684136
a[1]= 0.27026242360103137
a[2]= 0.6705225084244828
a[3]= 0.8994444352553269
a[4]= 0.9389234912589952

*/
