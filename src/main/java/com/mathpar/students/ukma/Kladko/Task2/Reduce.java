/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Kladko.Module2;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class Reduce 
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
            System.out.println("rank = " + rank + "; arr[" + i + "] = " + array[i] + ";");
        }

        System.out.println();

        int res[] = new int[size];

        WORLD.reduce(array, res, size, MPI.INT, MPI.SUM, 0);

        Thread.sleep(size * rank);
        if (rank == 0) 
        {
            for (int i = 0; i < res.length; i++) System.out.print(res[i] + " ");
            System.out.println();
        }

        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 4 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/Reduce
Output:
rank = 0; arr[0] = 0;
rank = 0; arr[1] = 1;
rank = 0; arr[2] = 2;
rank = 0; arr[3] = 3;

0 4 8 12 
rank = 1; arr[0] = 0;
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

*/