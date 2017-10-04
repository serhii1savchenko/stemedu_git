package com.mathpar.students.ukma17i41;

import java.util.Arrays;
import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

// to run project execute the command bellow
// openmpi/bin/mpirun -np 2 java -cp /home/YOUR-PATH-TO/stemedu/target/classes  com/mathpar/students/ukma17i41/FirstStep 2
// example 
// openmpi/bin/mpirun -np 2 java -cp /home/myname/projects/stemedu/target/classes  com/mathpar/students/ukma17i41/FirstStep 2

public class FirstStep {

    public static void HelloWorldParallel(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        MPI.Finalize();
    }

    public static void COMM_NEW(String[] args) throws MPIException {
        MPI.Init(args);
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(new int[] {0, 1});
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);
        int mysize = COMM_NEW.getSize();
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

    public static void main(String[] args) throws MPIException {
        HelloWorldParallel(args);
    }
}
