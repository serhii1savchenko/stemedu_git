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
public class Scatter 
{
    public static void main(String[] args) throws MPIException 
    {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();

        int procNum = WORLD.getSize();
        int[] arr = new int[procNum];

        if (rank == 0) 
        {
            System.out.println("Elements:");
            for (int i = 0; i < procNum; i++)
            {
                arr[i] = i;
                System.out.print(arr[i] + " ");
            }

            System.out.println();
        }
        
        System.out.println();

        int[] res = new int[procNum];

        WORLD.barrier();
        WORLD.scatter(arr, 1, MPI.INT, res, 1, MPI.INT, 0);

        System.out.println("Proc #" + rank + " has received:");

        for (int el : res) System.out.print(el + " ");

        System.out.println();        
        System.out.println();


        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 2 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/Scatter 4
Output:
Elements:
0 1 

Proc #0 has been received:
0 0 

Proc #1 has been received:
1 0 

*/