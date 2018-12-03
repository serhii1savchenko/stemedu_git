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
public class Scatterv 
{
    public static void main(String[] args) throws MPIException 
    {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();
        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];

        if (rank == 0) 
        {
            System.out.println("Elements:");
            for (int i = 0; i < size; i++)
            {
                arr[i] = i;
                System.out.print(arr[i] + " ");
            }

            System.out.println();
        }

        int[] res = new int[size];

        int sendSizes[] = new int[]{3, 2, 1, 1}; // depends on proc num
        int offsets[] = new int[]{0, 1, 2, 0};   // depends on proc num

        WORLD.scatterv(arr, sendSizes, offsets, MPI.INT, res, size, MPI.INT, 0);
        WORLD.barrier();

        System.out.println("Proc #" + rank + " has received:");

        for (int el : res) System.out.print(el + " ");

        System.out.println();

        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 4 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/Scatterv 8
Output:
Proc #3 has received:
0 0 0 0 0 0 0 0 
Elements:
0 1 2 3 4 5 6 7 
Proc #0 has received:
0 1 2 0 0 0 0 0 
Proc #1 has received:
1 2 0 0 0 0 0 0 
Proc #2 has received:
2 0 0 0 0 0 0 0 


*/