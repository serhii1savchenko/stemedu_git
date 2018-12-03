package com.mathpar.students.ukma.Fedorak.Module2; 

import java.math.BigInteger;
import java.util.Random;
import mpi.*;
public class MPI2_5 {
public static void main(String[] args)
throws MPIException {
MPI.Init(args);
int myrank = MPI.COMM_WORLD.getRank();
int n = Integer.parseInt(args[0]);
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
mpirun -np 4 --hostfile hostfile java -cp class MPI2_5 10

a[0]= 0.42360238825361074
a[1]= 0.7646439146166387
a[2]= 0.19775972024935995
a[3]= 0.7382274134461858
a[4]= 0.2518351538335941
a[5]= 0.3528723932035326
a[6]= 0.8579779580592625
a[7]= 0.03149230993677676
a[8]= 0.6934906442521177
a[9]= 0.4416060786310885
a[0]= 0.42360238825361074
a[1]= 0.7646439146166387
a[2]= 0.19775972024935995
a[3]= 0.7382274134461858
a[4]= 0.2518351538335941
a[5]= 0.3528723932035326
a[6]= 0.8579779580592625
a[7]= 0.03149230993677676
a[8]= 0.6934906442521177
a[9]= 0.4416060786310885
a[0]= 0.42360238825361074
a[1]= 0.7646439146166387
a[2]= 0.19775972024935995
a[3]= 0.7382274134461858
a[4]= 0.2518351538335941
a[5]= 0.3528723932035326
a[6]= 0.8579779580592625
a[7]= 0.03149230993677676
a[8]= 0.6934906442521177
a[9]= 0.4416060786310885
a[0]= 0.42360238825361074
a[1]= 0.7646439146166387
a[2]= 0.19775972024935995
a[3]= 0.7382274134461858
a[4]= 0.2518351538335941
a[5]= 0.3528723932035326
a[6]= 0.8579779580592625
a[7]= 0.03149230993677676
a[8]= 0.6934906442521177
a[9]= 0.4416060786310885


*/
