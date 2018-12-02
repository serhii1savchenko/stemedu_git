package com.mathpar.students.ukma.Bohachek;

import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;

public class MPI_2_4_TestISendAndIRecv {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);


        int myrank = MPI.COMM_WORLD.getRank();

        int elemNum = Integer.parseInt(args[0]);
        IntBuffer buffer = MPI.newIntBuffer(elemNum);

        MPI.COMM_WORLD.barrier();

        int tag = 3000;

        if (myrank == 0) {
            for (int i = 0; i < elemNum; i++)
                buffer.put(new Random().nextInt(10));

            int procNum = MPI.COMM_WORLD.getSize();
            for (int i = 1; i < procNum; i++)
                MPI.COMM_WORLD.iSend(buffer, buffer.capacity(), MPI.INT, i, tag);

            System.out.println("proc num " + myrank + " Array sent.");
        } else {
            MPI.COMM_WORLD.recv(buffer, buffer.capacity(), MPI.INT, 0, tag);

            System.out.println("proc num " + myrank + " Array received.");
        }

        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_2_4_TestISendAndIRecv.java
mpirun java MPI_2_4_TestISendAndIRecv 4

proc num 0 Array sent.
proc num 1 Array received.
proc num 2 Array received.
proc num 3 Array received.

*/
