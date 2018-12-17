/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Kladko.Module2;

import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class SendRecv 
{
    public static void main(String[] args) throws MPIException 
    {    
        MPI.Init(args);
        final Intracomm WORLD = MPI.COMM_WORLD;
        WORLD.barrier();
        
        final int arraySize = Integer.parseInt(args[0]);
        double[] array = new double[arraySize];
        
        final int myRank = WORLD.getRank();
        final int msgTag = 3000;

        if (myRank == 0) 
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
            for (int procNum = 1; procNum < WORLD.getSize(); ++procNum)
                WORLD.send(array, arraySize, MPI.DOUBLE, procNum, msgTag);
            System.out.println("Proc #" + myRank +" array was sent\n");
        } 
        else 
        {
            WORLD.recv(array, arraySize, MPI.DOUBLE, 0, msgTag);
            System.out.println("Proc #" + myRank +" array was received\n");
        }

        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 2 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task2/SendRecv 5
Output:
Array = [ 0.8395084009134013, 0.46388194733514265, 0.10964041959177695, 0.8856343702496083, 0.9402331335272038 ]
Proc #0 array was sent

Proc #1 array was received

*/