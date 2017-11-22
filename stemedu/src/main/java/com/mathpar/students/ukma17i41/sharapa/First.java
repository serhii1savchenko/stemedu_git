/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.sharapa;

import java.nio.IntBuffer;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

public class First {
    public static void Task1(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World" + "\n");
        MPI.Finalize();
    }
    

    public static void Task2(String[] args) throws MPIException {
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
    " Масив відправлено" + "\n");
} else {
   MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
   for (int i = 0; i < n; i++) {
     System.out.println("a[" + i + "]= " + a[i]);
   }
   System.out.println("Proc num " + myrank +
     " Масив прийнято" + "\n");
}
MPI.Finalize();
    }
public static void Task3(String[] args) throws MPIException {
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
                                  " Масив відправлено");
  } else {
    MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);
      System.out.println("proc num = " + myrank +
                                  " Масив прийнято");
  }
  MPI.Finalize();
 }


public static void Task4(String[] args) throws MPIException {
        MPI.Init(args);
    int myrank = MPI.COMM_WORLD.getRank();
    int n = Integer.parseInt(args[0]);
    double[] a = new double[n];
    if (myrank == 0) {
      for (int i = 0; i < n; i++) {
        a[i] = new Random().nextDouble();
        System.out.println("a[" + i + "]= " + a[i]);
} } 
    MPI.COMM_WORLD.barrier();
    MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
    if (myrank != 0) {
      for (int i = 0; i < n; i++) {
        System.out.println("a[" + i + "]= " + a[i]);
} } 
    MPI.Finalize();
    }
        
public static void Task5(String[] args) throws MPIException {
        MPI.Init(args);
    int myrank = MPI.COMM_WORLD.getRank();
    int np = MPI.COMM_WORLD.getSize();
    int n = Integer.parseInt(args[0]);
    int[] a = new int[n];
    for(int i = 0; i < n; i++) a[i] = myrank;
    int[] q = new int[n*np];
    MPI.COMM_WORLD.gather(a, n, MPI.INT, q, n, MPI.INT, np-1);
    if(myrank == np-1) {
      for(int i = 0; i < q.length; i++)
        System.out.println("  " + q[i]);
    }
    MPI.Finalize();
    }


    
public static void main(String[] args) throws MPIException {
        
        //Task1(args);
        //openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sharapa/First 1
        //Proc num 1 Hello World
        //Proc num 0 Hello World
        
        //Task2(args);
        //openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sharapa/First 1
        //a[0]= 0.2357351375011465
        //Proc num 0 Масив відправлено
        //a[0]= 0.2357351375011465
        //Proc num 1 Масив прийнято
        
        //Task3(args);
        //openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sharapa/First 1
        //proc num = 1 Масив прийнято proc num = 0 Масив відправлено
        
        //Task4(args);
        //openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sharapa/First 1
        //a[0]= 0.06962633087757919
        //a[0]= 0.06962633087757919
        
        //Task5(args);
        //openmpi/bin/mpirun -np 2 java -cp /home/vlad/Documents/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sharapa/First 1
        //0
        //1

}
    
}
