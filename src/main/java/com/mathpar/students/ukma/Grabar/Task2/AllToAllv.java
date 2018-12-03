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
public class AllToAllv 
{
    public static void main(String[] args) throws MPIException, InterruptedException 
    {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();
        int size = WORLD.getSize();

        Integer array[] = new Integer[size];

        for (int i = 0; i < size; ++i)
        {
            array[i] = i;
            System.out.println("rank = " + rank + 
                    "; arr[" + i + "] = " + array[i] + ";");
        }

        System.out.println();

        Integer res[] = new Integer[size];

        int sendSizes[] = new int[size];
        int recvSizes[] = new int[size];

        Arrays.fill(sendSizes, 1);
        Arrays.fill(recvSizes, 1);

        int sendOffsets[] = new int[size];
        int recvOffsets[] = new int[size];

        for (int i = 0; i < size; i++)
        {
            sendOffsets[i] = i;
            recvOffsets[i] = i;
        }

        WORLD.allToAllv(array, sendSizes, sendOffsets, MPI.INT, res,recvSizes, recvOffsets, MPI.INT);

        Thread.sleep(size * rank);

        for (int i = 0; i < size; i++)
            System.out.println("rank = " + rank + "; send = " + array[i] + "; recv = " + res[i]);

        System.out.println();

        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 4 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/AllToAllv
Output:
rank = 0; arr[0] = 0;
rank = 0; arr[1] = 1;
rank = 0; arr[2] = 2;
rank = 0; arr[3] = 3;

rank = 0; send = 0; recv = 0rank = 1; arr[0] = 0;
rank = 1; arr[1] = 1;
rank = 1; arr[2] = 2;
rank = 1; arr[3] = 3;

rank = 2; arr[0] = 0;
rank = 2; arr[1] = 1;
rank = 2; arr[2] = 2;
rank = 2; arr[3] = 3;

rank = 3; arr[0] = 0;
rank = 3; arr[1] = 1;
rank = 3; arr[2] = 2;
rank = 3; arr[3] = 3;


rank = 0; send = 1; recv = 0
rank = 0; send = 2; recv = 0
rank = 0; send = 3; recv = 0

rank = 1; send = 0; recv = 1
rank = 1; send = 1; recv = 1
rank = 1; send = 2; recv = 1
rank = 1; send = 3; recv = 1

rank = 2; send = 0; recv = 2
rank = 2; send = 1; recv = 2
rank = 2; send = 2; recv = 2
rank = 2; send = 3; recv = 2

rank = 3; send = 0; recv = 3
rank = 3; send = 1; recv = 3
rank = 3; send = 2; recv = 3
rank = 3; send = 3; recv = 3

*/