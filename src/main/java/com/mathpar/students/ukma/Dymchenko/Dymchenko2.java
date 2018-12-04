package com.mathpar.students.ukma.Dymchenko;

import mpi.*;

import java.util.Random;

public class Dymchenko2 {
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
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko2 2

a[0]= 0.952403330496274
a[1]= 0.26485262310331126
Proc num 0 Array sent

a[0]= 0.952403330496274
a[1]= 0.26485262310331126
Proc num 1 Array received

*/
