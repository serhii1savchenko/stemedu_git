/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p1;

import mpi.*;

/**
 *
 * @author vmudrij
 */
public class HelloWorld {
    public static void main(String[] args) throws MPIException {
        // Initialize a parallel part
        MPI.Init(args);

        // Define a number of a process
        int myrank = MPI.COMM_WORLD.getRank();

        System.out.println("Proc num " + myrank + " Hello World");

        // Completion a parallel part
        MPI.Finalize();
    }
}

// Result (4 processors):
//        Proc num 3 Hello World
//        Proc num 2 Hello World
//        Proc num 1 Hello World
//        Proc num 0 Hello World
