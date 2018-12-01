package com.mathpar.students.ukma.Grushka.Module2Part1;

import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;

public class TestISendandIRecv {
    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors and processor amount in a group
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        // Input an array size
        int n = 16;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        IntBuffer b = MPI.newIntBuffer(n);
        // Processors synchronization
        MPI.COMM_WORLD.barrier();
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                b.put(new Random().nextInt(10));
            }
            for (int i = 1; i < np; i++) {
                MPI.COMM_WORLD.iSend(b, b.capacity(), MPI.INT, i, 3000);
            }
            System.out.println("proc num = " + myrank +
                    " array was sent");
        } else {
            MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);
            System.out.println("proc num = " + myrank +
                    " array was received");
        }

        MPI.Finalize();
    }
}

//  Result (3 processors):
//        proc num = 0 array was sent
//        proc num = 1 array was received
//        proc num = 2 array was received