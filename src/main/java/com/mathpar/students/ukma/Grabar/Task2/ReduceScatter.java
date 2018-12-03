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
public class ReduceScatter 
{
    public static void main(String[] args) throws MPIException, InterruptedException 
    {
        MPI.Init(args);

        Intracomm WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();
        int size = WORLD.getSize();

        int array[] = new int[size];

        for (int i = 0; i < size; ++i)
        {
            array[i] = i;
            System.out.println("rank = " + rank + "; array[" + i + "] = " + array[i] + ";");
        }

        System.out.println();

        int res[] = new int[1];
        int recvSizes[] = new int[size];
        Arrays.fill(recvSizes, 1);

        WORLD.reduceScatter(array, res, recvSizes, MPI.INT, MPI.SUM);
        Thread.sleep(res.length * rank);

        System.out.println("\nProc #" + rank + " has received:");

        for (int el : res) System.out.print(el + " ");
        System.out.println();

        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 4 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/ReduceScatter
Output:
rank = 0; array[0] = 0;
rank = 1; array[0] = 0;
rank = 0; array[1] = 1;
rank = 1; array[1] = 1;
rank = 1; array[2] = 2;
rank = 1; array[3] = 3;

rank = 0; array[2] = 2;
rank = 0; array[3] = 3;

rank = 2; array[0] = 0;rank = 3; array[0] = 0;
rank = 3; array[1] = 1;
rank = 2; array[1] = 1;
rank = 2; array[2] = 2;
rank = 2; array[3] = 3;


rank = 3; array[2] = 2;
rank = 3; array[3] = 3;


Proc #0 has received:
0 

Proc #1 has received:
4 

Proc #2 has received:
8 

Proc #3 has received:
12 

*/