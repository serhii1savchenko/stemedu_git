package com.mathpar.students.ukma.Kladko;


import mpi.*;
//mpirun -np 2 java -cp /home/teacher/stemedu/target/classes com.mathpar.students.ukma.Kladko/HelloWorld
public class HelloWorld{
public static void main(String[] args)
throws MPIException { 
MPI.Init(args);
int myrank = MPI.COMM_WORLD.getRank();
System.out.println("Proc num " + myrank + " Hello World");
 
MPI.Finalize();
}
}