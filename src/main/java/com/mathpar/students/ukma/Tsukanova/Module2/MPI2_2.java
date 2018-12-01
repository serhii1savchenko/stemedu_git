package com.mathpar.students.ukma.Tsukanova.Module2;

import mpi.*;

import java.util.Random;

public class MPI2_2 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        final Intracomm WORLD = MPI.COMM_WORLD;
        WORLD.barrier();
        final int arraySize = Integer.parseInt(args[0]);
        double[] array = new double[arraySize];
        final int myRank = WORLD.getRank();
        final int msgTag = 3000;
        if (myRank == 0) {
            for (int i = 0; i < arraySize; ++i) {
                array[i] = new Random().nextDouble();
                System.out.println("a[" + i + "]= " + array[i]);
           }
            for (int procNum = 1; procNum < WORLD.getSize(); ++procNum)
               WORLD.send(array, arraySize, MPI.DOUBLE, procNum, msgTag);
            System.out.println("Proc num " + myRank +" Array sent\n");
        } else {
            WORLD.recv(array, arraySize, MPI.DOUBLE, 0, msgTag);
            for (int i = 0; i < arraySize; ++i)
                System.out.println("a[" + i + "]= " + array[i]);
            System.out.println("Proc num " + myRank +" Array received\n");
        }
        MPI.Finalize();
    }
}

/*
COMMAND
 /home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI2_2 2

RESULT
a[0]= 0.7273066806177277
a[1]= 0.8694904983482503
Proc num 0 Array sent

a[0]= 0.7273066806177277
a[1]= 0.8694904983482503
Proc num 2 Array received

a[0]= 0.7273066806177277
a[1]= 0.8694904983482503
a[0]= 0.7273066806177277
a[1]= 0.8694904983482503
Proc num 1 Array received

Proc num 3 Array received
*/
