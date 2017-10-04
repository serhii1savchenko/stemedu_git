/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.beyrak;

import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author maria
 */

/*
To run:
openmpi/bin/mpirun -np 2 java -cp /home/maria/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/beyrak/Tasks 2
*/
public class Tasks {
    
    /*
    Output:
    Proc num 0 Hello World
    Proc num 1 Hello World
    */
    public static void Task1(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        MPI.Finalize();
}
    
    public static void main(String[] args) throws MPIException {
        Task1(args);
    }
}
