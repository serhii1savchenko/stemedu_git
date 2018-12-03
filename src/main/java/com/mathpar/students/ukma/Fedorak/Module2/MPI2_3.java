package com.mathpar.students.ukma.Fedorak.Module2; 
import java.util.Random;
import mpi.*;
public class MPI2_3 {
public static void main(String[] args)
throws MPIException {
	MPI.Init(args);
	int myrank = MPI.COMM_WORLD.getRank();
	int np = MPI.COMM_WORLD.getSize();
	int n = Integer.parseInt(args[0]);
	double[] a = new double[n];
	MPI.COMM_WORLD.barrier();
	if (myrank == 0) {
	for (int i = 0; i < n; i++) {
	a[i] = (new Random()).nextDouble();
	System.out.println("a[" + i + "]= " + a[i]);
	}
	for (int i = 1; i < np; i++) {
	MPI.COMM_WORLD.send(a, n, MPI.DOUBLE, i, 3000);
	}
	System.out.println("Proc num " + myrank +
	" Array Sent" + "\n");
	} else {
	MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
	for (int i = 0; i < n; i++) {
	System.out.println("a[" + i + "]= " + a[i]);
	}
	System.out.println("Proc num " + myrank +
	" Array Received" + "\n");
	}
	MPI.Finalize();
	}
}
/*
mpirun -np 4 --hostfile hostfile java -cp class MPI2_3 10


a[0]= 0.10218505754449758
a[1]= 0.5836774843874992
a[2]= 0.7791857501467766
a[3]= 0.3786658364988539
a[4]= 0.4818176923202653
a[5]= 0.6649104452807579
a[6]= 0.7412135488784791
a[7]= 0.08989076089693926
a[8]= 0.18364010791327923
a[9]= 0.20808285972561602
Proc num 0 Array Sent

a[0]= 0.10218505754449758
a[1]= 0.5836774843874992
a[2]= 0.7791857501467766
a[3]= 0.3786658364988539a[0]= 0.10218505754449758

a[4]= 0.4818176923202653
a[1]= 0.5836774843874992
a[5]= 0.6649104452807579
a[6]= 0.7412135488784791
a[2]= 0.7791857501467766
a[7]= 0.08989076089693926
a[8]= 0.18364010791327923a[3]= 0.3786658364988539
a[4]= 0.4818176923202653

a[5]= 0.6649104452807579
a[9]= 0.20808285972561602
Proc num 2 Array Received

a[6]= 0.7412135488784791
a[7]= 0.08989076089693926
a[8]= 0.18364010791327923
a[9]= 0.20808285972561602
Proc num 1 Array Received

a[0]= 0.10218505754449758
a[1]= 0.5836774843874992
a[2]= 0.7791857501467766
a[3]= 0.3786658364988539
a[4]= 0.4818176923202653
a[5]= 0.6649104452807579
a[6]= 0.7412135488784791
a[7]= 0.08989076089693926
a[8]= 0.18364010791327923
a[9]= 0.20808285972561602
Proc num 3 Array Received


*/
