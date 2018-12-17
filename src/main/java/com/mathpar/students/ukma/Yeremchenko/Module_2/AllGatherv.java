/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Yeremchenko.Module_2;

import java.util.Arrays;
import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class AllGatherv 
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

        // How many elements to receive from each proc
        int recvCount[] = new int[procNum];
        Arrays.fill(recvCount, size);

        int offsets[] = new int[]{0, 2, 5};

        WORLD.allGatherv(arr, size, MPI.INT, res, recvCount, offsets, MPI.INT);

        Thread.sleep(size * rank);

        System.out.println("Proc #" + rank + " has received:");

        for (int el : res) System.out.print(el + " ");
        System.out.println();        
        System.out.println();


        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 2 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Yeremchenko/Module_2/AllGatherv 4
Output:
Proc #0 has been received:
0 0 0 0 1 1 0 0 

Proc #1 has been received:
0 0 1 1 1 1 0 0 


*/