/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p1;

import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author vmudrij
 */
// TestCreateIntracomm
public class M_2_18 {
    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);

        // Defining a new group of running processors
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(new int[] {0, 1});

        // Creating a new communicator
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);

        int myrank = COMM_NEW.getRank();
        int n = 4;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        double[] a = new double[n];

        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }

        COMM_NEW.barrier();

        // Applying function bcast to new communicator
        COMM_NEW.bcast(a, a.length, MPI.DOUBLE, 0);

        if (myrank != 0) {
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }

        // Completion a parallel part
        MPI.Finalize();
    }
}

// OUTPUT:
// array[0]= 0.5985778886675682
// array[1]= 0.11498097463049584
// array[2]= 0.996302793728519
// array[3]= 0.9965200144294099
// array[0]= 0.5985778886675682
// array[1]= 0.11498097463049584
// array[2]= 0.996302793728519
// array[3]= 0.9965200144294099
