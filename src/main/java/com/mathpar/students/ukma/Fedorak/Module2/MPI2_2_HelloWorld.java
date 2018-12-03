package com.mathpar.students.ukma.Fedorak.Module2; 

import mpi.*;
public class MPI2_2_HelloWorld {

	public static void main(String[] args) {
		try{
			MPI.Init(args);
			int myrank = MPI.COMM_WORLD.getRank();
			System.out.println("Proc num " + myrank + " Hello World");
			MPI.Finalize();
		}catch(Exception e){
			
		}
	}
	
}
/*
mpirun -np 10 --hostfile hostfile java -cp class MPI2_2_HelloWorld

Proc num 1 Hello World
Proc num 2 Hello World
Proc num 8 Hello World
Proc num 3 Hello World
Proc num 0 Hello World
Proc num 6 Hello World
Proc num 9 Hello World
Proc num 5 Hello World
Proc num 4 Hello World
Proc num 7 Hello World

*/
