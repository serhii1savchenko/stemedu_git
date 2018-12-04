package com.mathpar.students.ukma.Dymchenko;

import java.nio.IntBuffer;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class Dymchenko11 {

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;
        int rank = WORLD.getRank();
        int size = WORLD.getSize();
        IntBuffer buffer = MPI.newIntBuffer(size);
        for (int i = 0; i < buffer.capacity(); ++i)
        {
            buffer.put(i);
            System.out.println("rank = " + rank + "; buffer[" + i + "] = " + buffer.get(i));
        }
        System.out.println();
        IntBuffer res = MPI.newIntBuffer(size);
        WORLD.allToAll(buffer, 1, MPI.INT, res, 1, MPI.INT);
        Thread.sleep(size * rank);
        for (int i = 0; i < res.capacity(); i++)
            System.out.println("rank = " + rank + "; send = " + buffer.get(i) + "; recv = " + res.get(i));
        System.out.println();
        MPI.Finalize();
    }
}

/*
mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko11
rank = 0; buffer[0] = 0
rank = 0; buffer[1] = 1

rank = 1; buffer[0] = 0
rank = 1; buffer[1] = 1

rank = 0; send = 0; recv = 0
rank = 0; send = 1; recv = 0

rank = 1; send = 0; recv = 1
rank = 1; send = 1; recv = 1

*/
