package com.mathpar.students.ukma.Grushka.Module2Part1;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

import java.util.Random;

public class TestCreateIntracomm {
    public static void main(String[] args) throws MPIException {
        // MPI definition
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

        MPI.Finalize();
    }
}


//mpirun -np 2 java -cp out/production/Module2Part1 TestCreateIntracomm

//   Result (2 processors & n=4):
//a[0]= 0.8544217153091593
//a[1]= 0.9638144099558331
//a[2]= 0.09980886820190116
//a[3]= 0.8212045567475573
//a[0]= 0.8544217153091593
//a[1]= 0.9638144099558331
//a[2]= 0.09980886820190116
//a[3]= 0.8212045567475573
