/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Grabar.Task2;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author Igor
 */
public class AllToAll 
{
    public static void main(String[] args) throws MPIException, InterruptedException 
    {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();
        int size = WORLD.getSize();
        IntBuffer buffer = MPI.newIntBuffer(size);

        for (int i = 0; i < buffer.capacity(); ++i)
        {
            buffer.put(i);
            System.out.println("rank = " + rank + 
                    "; buffer[" + i + "] = " + buffer.get(i) + ";");
        }

        System.out.println();

        IntBuffer res = MPI.newIntBuffer(size);
        WORLD.allToAll(buffer, 1, MPI.INT, res, 1, MPI.INT);
        Thread.sleep(size * rank);

        for (int i = 0; i < res.capacity(); i++)
            System.out.println("rank = " + rank + 
                    "; send = " + buffer.get(i) + 
                    "; recv = " + res.get(i) + ";");

        System.out.println();
        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 4 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/AllToAll
Outptu:
rank = 0; buffer[0] = 0;
rank = 0; buffer[1] = 1;
rank = 0; buffer[2] = 2;rank = 1; buffer[0] = 0;
rank = 0; buffer[3] = 3;

rank = 1; buffer[1] = 1;

rank = 1; buffer[2] = 2;
rank = 1; buffer[3] = 3;

rank = 3; buffer[0] = 0;
rank = 3; buffer[1] = 1;
rank = 3; buffer[2] = 2;
rank = 3; buffer[3] = 3;

rank = 2; buffer[0] = 0;
rank = 2; buffer[1] = 1;
rank = 2; buffer[2] = 2;
rank = 2; buffer[3] = 3;

rank = 0; send = 0; recv = 0;
rank = 0; send = 1; recv = 0;
rank = 0; send = 2; recv = 0;
rank = 0; send = 3; recv = 0;

rank = 1; send = 0; recv = 1;
rank = 1; send = 1; recv = 1;
rank = 1; send = 2; recv = 1;
rank = 1; send = 3; recv = 1;

rank = 2; send = 0; recv = 2;
rank = 2; send = 1; recv = 2;
rank = 2; send = 2; recv = 2;
rank = 2; send = 3; recv = 2;

rank = 3; send = 0; recv = 3;
rank = 3; send = 1; recv = 3;
rank = 3; send = 2; recv = 3;
rank = 3; send = 3; recv = 3;


*/