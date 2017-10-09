/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mathpar.students.ukma17i41.bosa;



import java.util.Arrays;
import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
/**
 *
 * @author sasha
 */


/*-----input------
openmpi/bin/mpirun -np 2 java -cp /home/sasha/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/bosa/HelloWorldParallel
*/


class HelloWorldParallel {
public static void main(String[] args)
throws MPIException {
MPI.Init(args);
int myrank = MPI.COMM_WORLD.getRank();
System.out.println("Proc num " + myrank + " Hello World");
MPI.Finalize();
}
}

/* --------result---------

Proc num 1 Hello World
Proc num 0 Hello World

*/



/*-----input------
openmpi/bin/mpirun -np 2 java -cp /home/sasha/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/bosa/Task1 3
*/

public class Task1 {
    
    private static void Example1(String[] args) throws MPIException{
        MPI.Init(args);
      int myRank=MPI.COMM_WORLD.getRank();
      int n=Integer.parseInt(args[0]);
      int []a=new int[n];
      if (myRank==0){
      Random rnd=new Random();
      for (int i=0; i<n; i++){
         a[i]=rnd.nextInt()%n;
      }

      MPI.COMM_WORLD.send(a, n, MPI.INT, 1, 0);
      System.out.println("rank=0");

      for(int i=0; i<n; i++)
       {
         System.out.println(a[i]);
       }
      }

      if (myRank==1){
        MPI.COMM_WORLD.recv(a, n, MPI.INT, 0, 0);
        System.out.println("rank=1");
      }
      MPI.Finalize();
      }
    
    public static void main(String[] args) throws MPIException{
       Example1(args);
    }
  }


/* -------result---------

a[0]= 0.13833425788281484
a[1]= 0.12457630151098487
a[2]= 0.7198124494007982
Proc num 0Array is sent

a[0]= 0.13833425788281484
a[1]= 0.12457630151098487
a[2]= 0.7198124494007982
Proc num 1Array is excepted

*/



/*-----input------
openmpi/bin/mpirun -np 2 java -cp /home/sasha/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/bosa/Task2 8
*/


class Task2 {
  private static void Example2(String[] args) throws MPIException{
      MPI.Init(args);
      int myRank=MPI.COMM_WORLD.getRank();
      int n=Integer.parseInt(args[0]);
      int []a=new int[n];

      if (myRank==0){
            Random rnd=new Random();
            for (int i=0; i<n; i++){
            a[i]=rnd.nextInt()%n;
               }
        System.out.println("rank=0\n");
            for(int i=0; i<n; i++)
            {
                System.out.println(a[i]);
            }
        MPI.COMM_WORLD.send(a, n, MPI.INT, 1, 0);
       }

       if (myRank==1){
          System.out.println("rank=1\n");   
          MPI.COMM_WORLD.recv(a, n, MPI.INT, 0, 0);
          System.out.println("Running on rank 1\n"); 
       }
       MPI.Finalize();
        }
  
 
   public static void main(String[] args) throws MPIException{
       Example2(args);
    }

}


/*-------result---------

rank=1
rank=0
3
4
0
2
4
0
5
-2
Running on rank 1

*/


/*-----input------
openmpi/bin/mpirun -np 2 java -cp /home/sasha/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/bosa/Task3 4 
*/

class Task3 {
  private static void Example3(String[] args) throws MPIException{
      MPI.Init(args);
      
      int myRank=MPI.COMM_WORLD.getRank();
      int n=Integer.parseInt(args[0]);
      int []a=new int[n];
      int []b=new int[n];
      if (myRank==0){
          Random rnd=new Random();
          for (int i=0; i<n; i++){
            a[i]=rnd.nextInt()%n;
           }
       MPI.COMM_WORLD.send(a, n, MPI.INT, 1, 0);
       MPI.COMM_WORLD.recv(b, n, MPI.INT, 1, 1);
       System.out.println("rank=0\n");
            for(int i=0; i<n; i++)
            {
                System.out.println(a[i]);
            }
      }
     if (myRank==1){
      Random rnd=new Random();
      for (int i=0; i<n; i++){
      b[i]=rnd.nextInt()%n;
       }
     MPI.COMM_WORLD.send(b, n, MPI.INT, 0, 1);
     MPI.COMM_WORLD.recv(a, n, MPI.INT, 0, 0);
     System.out.println("rank=1\n");
            for(int i=0; i<n; i++)
            {
                System.out.println(b[i]);
            }
 
        }
        MPI.Finalize();
  }
   public static void main(String[] args) throws MPIException{
       Example3(args);
    }

}


/*-------result--------

rank=1

rank=0

0
-3
-1
0
2
0
2
-1

*/


/*-----input------
openmpi/bin/mpirun -np 2 java -cp /home/sasha/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/bosa/TestCreateIntracomm 2
*/

class TestCreateIntracomm {
    public static void main(String[] args) throws
       MPIException {
       MPI.Init(args);
       mpi.Group g = MPI.COMM_WORLD.getGroup().incl(
       new int[]{0, 1});
       Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);
       int myrank = COMM_NEW.getRank();
       int n = Integer.parseInt(args[0]);
       double[] a = new double[n];
       if (myrank == 0){
         for (int i = 0; i < n; i++) {
           a[i] = new Random().nextDouble();
          }
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
       }
      COMM_NEW.barrier();
      COMM_NEW.bcast(a, a.length, MPI.DOUBLE, 0);
       if (myrank != 0)
         System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
       MPI.Finalize();

      }
}


/*---------result---------

myrank = 0: a = [0.13480596545595558, 0.0059302934638162386]
myrank = 1: a = [0.13480596545595558, 0.0059302934638162386]

*/