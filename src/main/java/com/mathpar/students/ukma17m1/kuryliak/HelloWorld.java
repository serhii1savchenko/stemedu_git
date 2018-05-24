package com.mathpar.students.ukma17m1.kuryliak;

import mpi.MPI;

import java.util.Random;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        int myRank=MPI.COMM_WORLD.getRank();
        int n=Integer.parseInt(args[0]);
        int []a=new int[n];
        if (myRank==0){
            Random rnd=new Random();
            for (int i=0; i<n; i++){
                a[i]=rnd.nextInt()%n;
            }
            MPI.COMM_WORLD.send(a, n, MPI.INT, 1, 0);
        }
        if (myRank==1){
            MPI.COMM_WORLD.recv(a, n, MPI.INT, 0, 0);
        }

    }
}
