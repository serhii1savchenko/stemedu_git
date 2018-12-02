/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p1;

import java.nio.IntBuffer;
import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;

/**
 *
 * @author vmudrij
 */
// com.mathpar.students.ukma.Zhyrkova.Module2Part1.ZhyrkovaTestAllToAll
public class M_2_12 {
    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);

        // Definition a processor amount
        int myrank = MPI.COMM_WORLD.getRank();

        int n = 2;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }

        // Creation an array of 4 elements
        IntBuffer b = MPI.newIntBuffer(4);
        b.put(0);
        b.put(1);
        b.put(2);
        b.put(3);

        for (int i = 0; i < 4; i++) {
            System.out.println("b[" + i + "] = " + b.get(i));
        }

        System.out.println("capacity b = " + b.capacity());

        IntBuffer q = MPI.newIntBuffer(4);

        MPI.COMM_WORLD.allToAll(b, n, MPI.INT, q, n, MPI.INT);

        for (int i = 0; i < q.capacity(); i++) {
            System.out.println("myrank = " + myrank
                    + " send=" + b.get(i) + " recv=" + q.get(i));
        }

        // Completion a parallel part
        MPI.Finalize();
    }
}
