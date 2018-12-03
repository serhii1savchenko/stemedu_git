package com.mathpar.students.ukma.Fedorak.Module2; 

import mpi.MPI;
import mpi.MPIException;
public class MPI2_7 {
public static void main(String[] args) throws MPIException {
		MPI.Init(args);
		int myrank = MPI.COMM_WORLD.getRank();
		int np = MPI.COMM_WORLD.getSize();
		int n = Integer.parseInt(args[0]);
		int[] a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = myrank;
		}
		int[] q = new int[n * np];
		MPI.COMM_WORLD.gatherv(a, n, MPI.INT, q,
			new int[]{n, n}, new int[]{0, 2},
			MPI.INT, np - 1);
		if (myrank == np - 1) {
		for (int i = 0; i < q.length; i++) {
		System.out.println(" " + q[i]);
		}
		}
		MPI.Finalize();
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
