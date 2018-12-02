package com.mathpar.students.ukma.Tsukanova.Module2;

import mpi.*;

import java.util.Random;

public class MPI2_17 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
        int[] ranks = new int[WORLD.getSize()];
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
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI2_17 4

RESULT
arr[0]= 0.6191855056954667
arr[1]= 0.3475434422842556
arr[2]= 0.43020781273496234
arr[3]= 0.8941852482535245



arr[0]= 0.6191855056954667
arr[1]= 0.3475434422842556
arr[2]= 0.43020781273496234
arr[3]= 0.8941852482535245
arr[0]= 0.6191855056954667
arr[1]= 0.3475434422842556
arr[2]= 0.43020781273496234
arr[3]= 0.8941852482535245
arr[0]= 0.6191855056954667
arr[1]= 0.3475434422842556
arr[2]= 0.43020781273496234
arr[3]= 0.8941852482535245
*/