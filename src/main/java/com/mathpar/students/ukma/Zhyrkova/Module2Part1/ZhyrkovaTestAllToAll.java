package com.mathpar.students.ukma.Zhyrkova.Module2Part1;

import mpi.*;

import java.nio.IntBuffer;

public class ZhyrkovaTestAllToAll {

    public static void main(String[] args) throws MPIException {
        // Initialization MPI
        MPI.Init(args);
        // Definition a processor amount
        int myrank = MPI.COMM_WORLD.getRank();

        int n = 2;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        // Creation an array of 4 elements
        IntBuffer b = MPI.newIntBuffer(4);
        b.put(0); b.put(1); b.put(2); b.put(3);
        for (int i = 0; i < 4; i++)
            System.out.println("b[" + i + "] = " + b.get(i));
        System.out.println("capacity b = " + b.capacity());
        IntBuffer q = MPI.newIntBuffer(4);
        MPI.COMM_WORLD.allToAll(b, n, MPI.INT, q, n, MPI.INT);
        for (int i = 0; i < q.capacity(); i++) {
            System.out.println("myrank = " + myrank +
                    " send=" + b.get(i) + " recv=" + q.get(i));
        }
        // Completion a parallel part
        MPI.Finalize();
    }
}

//mpirun -np 2 java -cp out/production/Module2Part1 ZhyrkovaTestAllToAll

// Output for amount of processors equals 2
//        b[0] = 0
//        b[1] = 1
//        b[2] = 2
//        b[3] = 3
//        capacity b = 4
//        b[0] = 0
//        b[1] = 1
//        b[2] = 2
//        b[3] = 3
//        capacity b = 4
//        myrank = 0 send=0 recv=0
//        myrank = 0 send=1 recv=1
//        myrank = 0 send=2 recv=0
//        myrank = 0 send=3 recv=1
//        myrank = 1 send=0 recv=2
//        myrank = 1 send=1 recv=3
//        myrank = 1 send=2 recv=2
//        myrank = 1 send=3 recv=3

