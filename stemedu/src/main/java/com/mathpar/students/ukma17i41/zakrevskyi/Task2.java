package com.mathpar.students.ukma17i41.zakrevskyi;

import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;

// Закревський Олександр, Task 2.4
//Output: 
//proc num = 0 Array sent
//proc num = 1 Array received



public class Task2 {
    public static void main(String[] args)
throws MPIException {
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
System.out.println("proc num = " + myrank +
" Array sent");
} else {
MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);
System.out.println("proc num = " + myrank +
" Array received");
}
MPI.Finalize();
}  
}
