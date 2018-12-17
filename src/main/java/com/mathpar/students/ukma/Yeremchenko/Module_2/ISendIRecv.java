/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Yeremchenko.Module_2;

import java.nio.IntBuffer;
import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class ISendIRecv 
{
    public static void main(String[] args) throws MPIException 
    {
        MPI.Init(args);
        Intracomm WORLD = MPI.COMM_WORLD;

        int myRank = WORLD.getRank();
        int elemNum = Integer.parseInt(args[0]);
        IntBuffer buffer = MPI.newIntBuffer(elemNum);

        WORLD.barrier();
        int tag = 250;

        if (myRank == 0) 
        {
            //Gen array
            for (int i = 0; i < elemNum; i++)
                buffer.put(new Random().nextInt(10));

            int procNum = WORLD.getSize();
            for (int i = 1; i < procNum; i++)
                WORLD.iSend(buffer, buffer.capacity(), MPI.INT, i, tag);

            System.out.println("Proc #" + myRank + " array was sent.");
        }
        else 
        {
            WORLD.recv(buffer, buffer.capacity(), MPI.INT, 0, tag);
            System.out.println("Proc #" + myRank + " array was received.");
        }

        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 2 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Yeremchenko/Module_2/ISendIRecv 5
Output:
Proc #1 array was received.
Proc #0 array was sent.

*/