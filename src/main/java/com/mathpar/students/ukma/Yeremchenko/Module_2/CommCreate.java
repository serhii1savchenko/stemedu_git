/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Yeremchenko.Module_2;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class CommCreate 
{
    public static void main(String[] args) throws MPIException 
    {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;

        int ranks[] = new int[WORLD.getSize()];
        for (int i = 0; i < ranks.length; i++) ranks[i] = i;

        Intracomm COMM_NEW = MPI.COMM_WORLD.create(MPI.COMM_WORLD.getGroup().incl(ranks));

        int rank = COMM_NEW.getRank();
        int size = Integer.parseInt(args[0]);
        double[] array = new double[size];

        if (rank == 0) 
        {
            System.out.print("Array = [ ");
            for (int i = 0; i < size; i++) 
            {
                array[i] = new Random().nextDouble();
                System.out.print(array[i]);
                if (i != size-1) System.out.print(", ");
            }
            System.out.println(" ]");
        }

        COMM_NEW.barrier();
        COMM_NEW.bcast(array, array.length, MPI.DOUBLE, 0);

        if (rank != 0) 
        {
            System.out.println();

            System.out.print("Recieved array = [ ");
            for (int i = 0; i < size; i++)
            {
                System.out.print(array[i]);
                if (i != size-1) System.out.print(", ");
            }
            System.out.println(" ]");
        }

        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 4 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Yeremchenko/Module_2/CommCreate 2
Output:
Array = [ 0.46311598435098345, 0.642552487474728 ]

Recieved array = [ 
Recieved array = [ 
Recieved array = [ 0.46311598435098345, 0.642552487474728 ]
0.46311598435098345, 0.642552487474728 ]
0.46311598435098345, 0.642552487474728 ]

*/