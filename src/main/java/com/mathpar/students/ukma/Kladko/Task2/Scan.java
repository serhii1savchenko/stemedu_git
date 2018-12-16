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

public class Scan 
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
            System.out.println("Rank = " + rank + "; Array[" + i + "] = " + array[i] + ";");
        }

        System.out.println();

        int res[] = new int[size];

        WORLD.scan(array, res, size, MPI.INT, MPI.SUM);
        Thread.sleep(size * rank);

        System.out.println("\nProc #" + rank + " has received:");

        for (int el : res) System.out.print(el + " ");
        System.out.println();
        
        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 4 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/Scan
Output:
Rank = 0; Array[0] = 0;
Rank = 0; Array[1] = 1;
Rank = 0; Array[2] = 2;
Rank = 0; Array[3] = 3;


Proc #0 has received:
0 1 Rank = 3; Array[0] = 0;
Rank = 3; Array[1] = 1;
Rank = 3; Array[2] = 2;
Rank = 3; Array[3] = 3;

Rank = 1; Array[0] = 0;
Rank = 1; Array[1] = 1;
Rank = 1; Array[2] = 2;
Rank = 1; Array[3] = 3;

Rank = 2; Array[0] = 0;
Rank = 2; Array[1] = 1;
Rank = 2; Array[2] = 2;
Rank = 2; Array[3] = 3;

2 3 

Proc #1 has received:
0 2 4 6 

Proc #2 has received:
0 3 6 9 

Proc #3 has received:
0 4 8 12 
*/