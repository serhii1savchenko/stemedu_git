package com.mathpar.students.ukma.Tsukanova;

import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;

public class MPI2_3 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        var WORLD = MPI.COMM_WORLD;
        int myRank = WORLD.getRank();
        int elemNum = Integer.parseInt(args[0]);
        IntBuffer buffer = MPI.newIntBuffer(elemNum);
        WORLD.barrier();
        int tag = 3000;
        if (myRank == 0) {
           for (int i = 0; i < elemNum; i++)
                buffer.put(new Random().nextInt(10));
            int procNum = WORLD.getSize();
            for (int i = 1; i < procNum; i++)
                WORLD.iSend(buffer, buffer.capacity(), MPI.INT, i, tag);
            System.out.println("proc num = " + myRank + " Array sent.");
        } else {
            WORLD.recv(buffer, buffer.capacity(), MPI.INT, 0, tag);
            System.out.println("proc num = " + myRank + " Array received.");
        }
        MPI.Finalize();
    }
}

/*
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI2_3 4

RESULT
proc num = 3 Array received.
proc num = 0 Array sent.
proc num = 2 Array received.
proc num = 1 Array received.
*/
