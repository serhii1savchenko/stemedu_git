/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Mudrij.m2p2;

import java.util.Arrays;
import java.math.BigInteger;
import java.util.Random;
import mpi.*;

/**
 *
 * @author vmudrij
 */
// com.mathpar.students.ukma.Zhyrkova.Module2Part1.ZhyrkovaTestBcast
public class M_3_1 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        
        int myrank = MPI.COMM_WORLD.getRank();
        
        // Input parameter - an array size
        int n = 4;
        
        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
        }
        
        double[] a = new double[n];
        
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
            }
            
            System.out.println("myrank = " + myrank + " : a = " + Arrays.toString(a));
        }
        
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        
        if (myrank != 0) {
            System.out.println("myrank = " + myrank + " : a = " + Arrays.toString(a));
        }
        
        MPI.Finalize();
    }
}

// OUTPUT:
// myrank = 0 : a = [0.03154582725826893, 0.9476896586791758, 0.9685250865622432, 0.3255315519228673]
// myrank = 3 : a = [0.03154582725826893, 0.9476896586791758, 0.9685250865622432, 0.3255315519228673]
// myrank = 2 : a = [0.03154582725826893, 0.9476896586791758, 0.9685250865622432, 0.3255315519228673]
// myrank = 1 : a = [0.03154582725826893, 0.9476896586791758, 0.9685250865622432, 0.3255315519228673]
