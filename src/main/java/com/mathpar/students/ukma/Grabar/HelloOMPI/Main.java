/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Grabar.HelloOMPI;
import mpi.*;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author igorko
 */

public class Main 
{
    public static void main(String[] args) throws MPIException { 
        MPI.Init(args);
        int myRank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myRank + " Hello World");
        MPI.Finalize();
    }
}
/*
Command:
mpirun -np 2 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/HelloOMPI/Main
Output:
Proc num 0 Hello World
Proc num 1 Hello World
*/