package com.mathpar.students.ukma.Dymchenko;


import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;

public class Dymchenko3 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
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
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko3 2
proc num = 0 Array sent.
proc num = 1 Array received.

*/
