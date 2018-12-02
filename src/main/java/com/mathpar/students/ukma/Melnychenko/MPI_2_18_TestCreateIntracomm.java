package com.mathpar.students.ukma.Bohachek;

import mpi.*;

import java.util.Random;


public class MPI_2_18_TestCreateIntracomm {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);


        int[] ranks = new int[ MPI.COMM_WORLD.getSize()];

        for (int i = 0; i < ranks.length; i++)
            ranks[i] = i;

        Intracomm COMM_NEW = MPI.COMM_WORLD.create(MPI.COMM_WORLD.getGroup().incl(ranks));

        int rank = COMM_NEW.getRank();

        int size = Integer.parseInt(args[0]);
        double[] arr = new double[size];

        if (rank == 0) {
            for (int i = 0; i < size; i++) {
                arr[i] = new Random().nextDouble();
                System.out.println("arr[" + i + "]= " + arr[i]);
            }
        }

        COMM_NEW.barrier();
        COMM_NEW.bcast(arr, arr.length, MPI.DOUBLE, 0);

        if (rank != 0) {
            System.out.println();

            for (int i = 0; i < size; i++)
                System.out.println("arr[" + i + "]= " + arr[i]);
        }

        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_2_18_TestCreateIntracomm.java
mpirun java MPI_2_18_TestCreateIntracomm 2

arr[0]= 0.6108520637158504
arr[1]= 0.5365825655216736


arr[0]= 0.6108520637158504
arr[1]= 0.5365825655216736
arr[0]= 0.6108520637158504
arr[1]= 0.5365825655216736
*/