package com.mathpar.students.ukma17m1.savchenko;

import mpi.MPI;

import java.util.Random;

public class HelloWorld {
	
	//mpirun -np 2 java -cp /home/serhii/Desktop/parall-progr-2017/stemedu/target/classes com/mathpar/students/ukma17m1/savchenko/HelloWorld

    public static void main(String[] args) throws Exception {
        
        MPI.Init(args);
        int myRank = MPI.COMM_WORLD.getRank();
        int n = 8; 
        
        int []a = new int[n];
        
        if (myRank==0){
            Random rnd=new Random();
            for (int i=0; i<n; i++){
                a[i]=rnd.nextInt()%n;
                System.out.println("a[i] = " + a[i]);
            }
            MPI.COMM_WORLD.send(a, n, MPI.INT, 1, 0);
        }
        if (myRank==1){
            MPI.COMM_WORLD.recv(a, n, MPI.INT, 0, 0);
        }
       
        MPI.Finalize();

    }
	
}
