package com.mathpar.students.ukma.Bohachek;

import java.util.Random;
import mpi.*;

public class MPI_2_5_TestBcast {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();

        int arrSize = Integer.parseInt(args[0]);
        double[] arr = new double[arrSize];

        if (rank == 0) {
            for (int i = 0; i < arrSize; i++) {
                arr[i] = new Random().nextDouble();

                System.out.println("a[" + i + "]= " + arr[i]);
            }
        }

        MPI.COMM_WORLD.barrier();

        MPI.COMM_WORLD.bcast(arr, arrSize, MPI.DOUBLE, 0);

        if (rank != 0) {
            for (int i = 0; i < arrSize; i++)
                System.out.println("a[" + i + "]= " + arr[i]);
        }

        MPI.Finalize();
    }
}

/*
mpirun --hostfile /home/elizabeth/hostfile -np 4 java -cp /home/elizabeth/stemedu/target/classes com.mathpar.students.ukma.Bohachek/MPI_2_5_TestBcast 4
a[0]= 0.9233188550446024
a[1]= 0.015049770689756437
a[2]= 0.3108244052804877
a[3]= 0.7421086504567953
a[0]= 0.9233188550446024
a[1]= 0.015049770689756437
a[2]= 0.3108244052804877
a[3]= 0.7421086504567953
a[0]= 0.9233188550446024
a[0]= 0.9233188550446024a
[1]= 0.015049770689756437
a[2]= 0.3108244052804877
a[3]= 0.7421086504567953
a[1]= 0.015049770689756437
a[2]= 0.3108244052804877
a[3]= 0.7421086504567953

*/
