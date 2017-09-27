/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.sukharskyi;

import java.util.Arrays;
import java.util.Random;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author acer
 */
public class PracticeTasks {
    // to run:
    //   openmpi/bin/mpirun -np 2 java -cp /home/acer/Documents/parralel_programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/PracticeTasks 5
    // output:
    //   My ranks is 0
    //   My ranks is 1
    //   5 random numbers: 
    //   -3 -3 4 0 -3
    private static void FirstTask(String[] args) throws MPIException
    {
        MPI.Init(args);        
        int myRank=MPI.COMM_WORLD.getRank();
        int n=Integer.parseInt(args[0]);
        int []a=new int[n];
        if (myRank==0){
            System.out.println("My ranks is " + myRank);
            Random rnd=new Random();
            System.out.println(n + " random numbers: ");
            for (int i=0; i<n; i++){
                a[i]=rnd.nextInt()%n;
                System.out.print(a[i] + " ");
            }
            System.out.println();
            MPI.COMM_WORLD.send(a, n, MPI.INT, 1, 0);
        }
        if (myRank==1){
            System.out.println("My ranks is " + myRank);
            MPI.COMM_WORLD.recv(a, n, MPI.INT, 0, 0);
        }
        MPI.Finalize();
    }
    
    // similar to first task
    private static void SecondTask(String[]args) throws MPIException
    {
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
        }
        if (myRank==1){
            MPI.COMM_WORLD.recv(a, n, MPI.INT, 0, 0);
        }
        MPI.Finalize();
    }
    
    // to run:
    //    openmpi/bin/mpirun -np 2 java -cp /home/acer/Documents/parralel_programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/PracticeTasks 5
    // output:
    //    Size of array will be 5
    //    Size of array will be 5
    //    In rank1
    //    Array B: In rank0
    //    Array A: 0 0 3 1 0 -3 -2 0 -3 0 
    //    OR
    //    Size of array will be 5
    //    Size of array will be 5
    //    In rank0
    //    Array A: In rank1
    //    Array B: 2 3 3 0 0 0 -4 0 0 
    //    0 
    //    Size of array will be 10
    //    Size of array will be 10
    //    In rank0
    //    Array A: 2 3 In rank1
    //    Array B: 7 8 6 5 -4 1 8 7 8 -8 -2 -2 5 0 2 -6 2 0
    private static void ThirdTask(String[] args) throws MPIException
    {
        MPI.Init(args);        
        int myRank=MPI.COMM_WORLD.getRank();
        int n=Integer.parseInt(args[0]);
        int []a=new int[n];
        int []b=new int[n];
        System.out.println("Size of array will be " + n);
        if (myRank==0){
            Random rnd=new Random();
            for (int i=0; i<n; i++){
                a[i]=rnd.nextInt()%n;
            }
            MPI.COMM_WORLD.send(a, n, MPI.INT, 1, 0);
            MPI.COMM_WORLD.recv(b, n, MPI.INT, 1, 1);
            System.out.print("In rank" +  myRank + "\nArray A: ");
            for (int i=0; i<n; i++){
                a[i]=rnd.nextInt()%n;
                System.out.print(a[i] + " ");
            }
            System.out.println();
        }
        if (myRank==1){
            Random rnd=new Random();
            for (int i=0; i<n; i++){
                b[i]=rnd.nextInt()%n;
            }
            MPI.COMM_WORLD.send(b, n, MPI.INT, 0, 1);
            MPI.COMM_WORLD.recv(a, n, MPI.INT, 0, 0);
            System.out.print("In rank" +  myRank + "\nArray B: ");
            for (int i=0; i<n; i++){
                System.out.print(b[i] + " ");
            }
            System.out.println();
        }
        MPI.Finalize();
    }
    
    //run:
    //  openmpi/bin/mpirun -np 2 java -cp /home/acer/Documents/parralel_programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/PracticeTasks 2
    //output:
    //    myrank = 0: a = [0.19453988198024585, 0.25414348637701156]
    //    myrank = 1: a = [0.19453988198024585, 0.25414348637701156]
    private static void FourthTask(String[] args) throws MPIException{
        MPI.Init(args);
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(new int[]{0, 1});
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
    
    public static void main(String[] args) throws MPIException {
        //Uncomment number of task you want to test and comment other
        //FirstTask(args);
        //SecondTask(args);
        //ThirdTask(args);
        FourthTask(args);
    }
}
