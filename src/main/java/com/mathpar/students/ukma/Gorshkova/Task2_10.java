package com.mathpar.students.ukma.Gorshkova;

import mpi.MPI;
import mpi.*;

public class Task2_10 {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        var WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();

        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];

        if (rank == 0) {
            for (int i = 0; i < size; i++){
                arr[i] = i;
                System.out.println("arr[" + i + "] = " + arr[i]);
            }

            System.out.println();
        }