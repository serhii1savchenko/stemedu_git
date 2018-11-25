/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p2;

import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

/**
 *
 * @author vmudrij
 */
// TestProbe
public class M_3_10 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int n = 4;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        if (rank == 0) {
            int[] array = new int[n];

            for (int i = 1; i < size; i++) {
                MPI.COMM_WORLD.send(array, n, MPI.INT, i, 1);
            }
        } else {
            Status st = null;

            while (st == null) {
                st = MPI.COMM_WORLD.probe(0, 1);
            }

            int[] array = new int[n];

            MPI.COMM_WORLD.recv(array, n, MPI.INT, 0, 1);
        }

        MPI.Finalize();
    }
}
