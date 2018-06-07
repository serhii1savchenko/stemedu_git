package com.mathpar.students.ukma17m1.oliynick;

import mpi.MPI;

import java.util.Random;

public class HelloWorld {

    //mpirun -np 2 java -cp /home/max/IdeaProjects/stemedu/target/classes com/mathpar/students/ukma17m1/oliynick/HelloWorld

    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        final int me = MPI.COMM_WORLD.getRank();
        final int cores = 8;
        final int[] arr = new int[cores];

        if (me == 0) {
            Random rnd = new Random();
            for (int i = 0; i < cores; i++) {
                arr[i] = rnd.nextInt() % cores;
                System.out.println(String.format("a[i] = %s", arr[i]));
            }
            MPI.COMM_WORLD.send(arr, cores, MPI.INT, 1, 0);
        }
        if (me == 1) {
            MPI.COMM_WORLD.recv(arr, cores, MPI.INT, 0, 0);
        }

        MPI.Finalize();
    }

}