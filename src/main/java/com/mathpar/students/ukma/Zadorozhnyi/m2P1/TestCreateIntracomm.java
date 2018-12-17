package com.mathpar.students.ukma.Zadorozhnyi.m2P1-PART1;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

import java.util.Random;

public class TestCreateIntracomm {
    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);
        // Defining a new group of running processors
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(
                new int[]{0, 1});
        // Creating a new communicator
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);
        int myrank = COMM_NEW.getRank();
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


// Output for two processors and n=4
//        a[0]= 0.47572440952136663
//        a[1]= 0.995912269136856
//        a[2]= 0.9339344304444064
//        a[3]= 0.7478147283151642
//        a[0]= 0.47572440952136663
//        a[1]= 0.995912269136856
//        a[2]= 0.9339344304444064
//        a[3]= 0.7478147283151642
