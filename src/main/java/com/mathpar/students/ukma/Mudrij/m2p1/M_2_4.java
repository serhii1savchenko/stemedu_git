/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p1;

import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;

/**
 *
 * @author vmudrij
 */
// TestISendAndIRecv
public class M_2_4 {
    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);

        // Definition a processor amount
        int myrank = MPI.COMM_WORLD.getRank();

        // Definition a processor amount in a group
        int np = MPI.COMM_WORLD.getSize();

        // Input parameter - an array size
        int n = 16;

        if (args.length != 0) {
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

            System.out.println("proc num = " + myrank + " array was sent");
        } else {
            MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);

            System.out.println("proc num = " + myrank + " array was received");
        }

        // Completion a parallel part
        MPI.Finalize();
    }
}

// Output:
// proc num = 0 array was sent
// proc num = 1 array was received
// proc num = 3 array was received
// proc num = 2 array was received
