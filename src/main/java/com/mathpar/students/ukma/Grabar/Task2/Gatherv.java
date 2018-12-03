/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Grabar.Task2;

import java.util.Arrays;
import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author Igor
 */
public class Gatherv 
{
    public static void main(String[] args) throws MPIException 
    {
        MPI.Init(args);

        Intracomm WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();

        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];
        Arrays.fill(arr, rank);

        int procNum = WORLD.getSize();
        int[] res = new int[size * procNum];

        int recvCount[] = new int[procNum];
        Arrays.fill(recvCount, size);

        int offsets[] = new int[]{0, 2, 5};

        WORLD.gatherv(arr, size, MPI.INT, res, recvCount, offsets, MPI.INT, procNum - 1);

        if(rank == procNum - 1) 
        {
            for(int i = 0; i < res.length; i++)
                System.out.print(res[i] + " ");
            System.out.println();
        }

        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 2 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/Gatherv 4
Output:
0 0 1 1 1 1 0 0 
*/