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
public class AllGather 
{
    public static void main(String[] args) throws MPIException, InterruptedException 
    {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();

        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];
        Arrays.fill(arr, rank);

        int procNum = WORLD.getSize();
        int[] res = new int[size * procNum];

        WORLD.allGather(arr, size, MPI.INT, res, size, MPI.INT);

        Thread.sleep(size * rank);
        System.out.println("Proc " + rank + " has been received:");

        for (int el : res) System.out.print(el + " ");
        System.out.println();

        System.out.println();

        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 2 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/AllGather 4
Output:
Proc 0 has been received:
0 0 0 0 1 1 1 1 

Proc 1 has been received:
0 0 0 0 1 1 1 1 


*/