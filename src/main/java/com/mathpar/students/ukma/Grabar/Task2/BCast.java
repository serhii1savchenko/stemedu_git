/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Grabar.Task2;

import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author Igor
 */
public class BCast 
{
    public static void main(String[] args) throws MPIException 
    {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();
        int arraySize = Integer.parseInt(args[0]);
        double[] array = new double[arraySize];

        if (rank == 0) 
        {
            //Generate array
            System.out.print("Array = [ ");
            for (int i = 0; i < arraySize; ++i) 
            {
                array[i] = new Random().nextDouble();
                System.out.print(array[i]);
                if (i != arraySize-1) System.out.print(", ");
            }
            System.out.println(" ]");
        }

        WORLD.barrier();

        WORLD.bcast(array, arraySize, MPI.DOUBLE, 0);

        if (rank != 0) 
        {
            //Generate array
            System.out.print("Recieved array = [ ");
            for (int i = 0; i < arraySize; ++i) 
            {
                System.out.print(array[i]);
                if (i != arraySize-1) System.out.print(", ");
            }
            System.out.println(" ]");
        }

        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 4 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/BCast 5
Output:
Array = [ 0.8130071391305072, 0.9063344995024508, 0.6717712827908022, 0.6904512583167001, 0.17985381768401043 ]
Recieved array = [ Recieved array = [ Recieved array = [ 0.8130071391305072, 0.9063344995024508, 0.6717712827908022, 0.6904512583167001, 0.17985381768401043 ]
0.8130071391305072, 0.9063344995024508, 0.6717712827908022, 0.6904512583167001, 0.17985381768401043 ]
0.8130071391305072, 0.9063344995024508, 0.6717712827908022, 0.6904512583167001, 0.17985381768401043 ]

*/