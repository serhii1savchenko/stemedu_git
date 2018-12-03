package com.mathpar.students.ukma.Fedorak.Module2;

import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;


public class MPI2_4 {
public static void main(String[] args) throws MPIException {
MPI.Init(args);
int myrank = MPI.COMM_WORLD.getRank();
int np = MPI.COMM_WORLD.getSize();
int n = Integer.parseInt(args[0]);
IntBuffer b = MPI.newIntBuffer(n);
MPI.COMM_WORLD.barrier();
if (myrank == 0) {
for (int i = 0; i < n; i++){
b.put(new Random().nextInt(10));
}
for (int i = 1; i < np; i++) {
MPI.COMM_WORLD.iSend(b, b.capacity(), MPI.INT, i, 3000);
}
	System.out.println("Proc num " + myrank +
	" Array Sent" + "\n");
} else {
MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);
	System.out.println("Proc num " + myrank +
	" Array Received" + "\n");
}
MPI.Finalize();
}
}
/*
mpirun -np 4 --hostfile hostfile java -cp class MPI2_4 10


Proc num 0 Array Sent
Proc num 1 Array Received

Proc num 2 Array Received


Proc num 3 Array Received


*/
