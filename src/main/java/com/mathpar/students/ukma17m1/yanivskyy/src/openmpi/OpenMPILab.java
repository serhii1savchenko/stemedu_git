/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yanivskyy.src.openmpi;

import java.util.Arrays;
import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author z1kses
 */
public class OpenMPILab {

    /**
     * @param args the command line arguments
     */
    
    // mpirun -n 2 java -cp "/home/z1kses/NetBeansProjects/mpi_naukma/build/classes" "openmpi/OpenMPILab"
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        final int size = MPI.COMM_WORLD.getSize();
        final int[] processes = new int[size];
        for (int i = 0; i < processes.length; i++) {
            processes[i] = i;
        }
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(processes);
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);

        int myrank = COMM_NEW.getRank();
        int n = Integer.parseInt(args[0]);
        double[] a = new double[n];
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
            }
            System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        }

        COMM_NEW.barrier();

        COMM_NEW.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0) {
            System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        }

        MPI.Finalize();
    }

}
