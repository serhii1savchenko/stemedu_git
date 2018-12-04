package com.mathpar.students.ukma.Dymchenko;

import mpi.*;
import java.util.Random;

public class Dymchenko17 {

    public static void main(String[] args) throws MPIException {
MPI.Init(args);
int myrank = MPI.COMM_WORLD.getRank();
int n = 4;
if (args.length != 0)
{
n = Integer.parseInt(args[0]);
}
double[] a = new double[n];
if (myrank == 0) {
for (int i = 0; i < n; i++) {
a[i] = new Random().nextDouble();
System.out.println("a[" + i + "]= " + a[i]);
}
}
MPI.COMM_WORLD.barrier();
MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
if (myrank != 0) {
for (int i = 0; i < n; i++) {
System.out.println("a[" + i + "]= " + a[i]);
}
}
MPI.Finalize();
}
}

/*
 mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko/Dymchenko17
a[0]= 0.1720905219746297
a[1]= 0.03311411468489878
a[2]= 0.3505334760775959
a[3]= 0.0948563726257029
a[0]= 0.1720905219746297
a[1]= 0.03311411468489878
a[2]= 0.3505334760775959
a[3]= 0.0948563726257029

*/