package com.mathpar.students.ukma.Grushka.Module2Part1;

import java.util.Random;
import mpi.*;

public class TestSendAndRecv {
    public static void main(String[] args)
            throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors and processor amount in a group
        final int myrank = MPI.COMM_WORLD.getRank();
        final int np = MPI.COMM_WORLD.getSize();
        // Input parameter - an array size
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        double[] a = new double[n];
        // Processors synchronization
        MPI.COMM_WORLD.barrier();
        // If the processor's number equals 0
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = (new Random()).nextDouble();
                System.out.println("a[" + i + "]= " + a[i]);
            }
            // Sending elements by 0s processor
            for (int i = 1; i < np; i++) {
                MPI.COMM_WORLD.send(a, n, MPI.DOUBLE, i, 3000);
            }
            System.out.println("Proc num " + myrank +
                    " array send" + "\n");
        } else {
            // Getting a message to a processor with number i from processor with number 0
            MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
            System.out.println("Proc num " + myrank +
                    " array recieved" + "\n");
        }

        MPI.Finalize();
    }
}


//mpirun -np 3 java -cp out/production/Module2Part1 TestSendAndRecv

//  Result (3 processors & n=4):
//
//a[0]= 0.994833745391606
//a[1]= 0.954564202510074
//a[2]= 0.900571754648766
//a[3]= 0.6187814832560263
//Proc num 0 array send
//a[0]= 0.994833745391606
//a[1]= 0.954564202510074
//a[2]= 0.900571754648766
//a[0]= 0.994833745391606
//a[3]= 0.6187814832560263
//Proc num 1 array recieved
//a[1]= 0.954564202510074
//a[2]= 0.900571754648766
//a[3]= 0.6187814832560263
//Proc num 2 array recieved