/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.fatieiev;

import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.IntBuffer;
import mpi.MPI;
import mpi.MPIException;
import java.util.Random;

/**
 *
 * @author ivan
 */
public class Tasks {

//command: /usr/bin/mpirun -np 2 java -cp /home/ivan/Documents/program/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/fatieiev/Tasks 2
//OUTPUT:
// Proc num 1 Hello World
// Proc num 0 Hello World
    public static void task1(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        MPI.Finalize();
    }

//command: /usr/bin/mpirun -np 2 java -cp /home/ivan/Documents/program/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/fatieiev/Tasks 2
//OUTPUT: 
//a[0]= 0.691339332552772
//a[1]= 0.06369337080826742
//Proc num 0 array send
//
//a[0]= 0.691339332552772
//a[1]= 0.06369337080826742
//Proc num 1 array accepted
    public static void task2(String[] args) throws MPIException {
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
            System.out.println("Proc num " + myrank + " array send" + "\n");
        } else {
            MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
            System.out.println("Proc num " + myrank + " array accepted" + "\n");
        }

        MPI.Finalize();
    }
    
    
//command: /usr/bin/mpirun -np 8 java -cp /home/ivan/Documents/program/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/fatieiev/Tasks 1000
//OUTPUT: 
//proc num = 1 array accepted
//
//proc num = 6 array accepted
//
//proc num = 7 array accepted
//
//proc num = 0 array send
//proc num = 5 array accepted
//proc num = 3 array accepted
//
//
//
//proc num = 4 array accepted
//
//proc num = 2 array accepted
    public static void task3(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        IntBuffer b = MPI.newIntBuffer(n);
        MPI.COMM_WORLD.barrier();
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                b.put(new Random().nextInt(10));
            }
            for (int i = 1; i < np; i++) {
                MPI.COMM_WORLD.iSend(b, b.capacity(), MPI.INT, i, 3000);
            }
            System.out.println("proc num = " + myrank + " array send\n");
        } else {
            MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);
            System.out.println("proc num = " + myrank + " array accepted\n");
        }

        MPI.Finalize();
    }
    
    
//command: /usr/bin/mpirun -np 2 java -cp /home/ivan/Documents/program/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/fatieiev/Tasks 5
//OUTPUT:  
//a[0]= 0.8725262656678667
//a[1]= 0.789550775254567
//a[2]= 0.03138225527352545
//a[3]= 0.08669497525356051
//a[4]= 0.9996715282822418
//a[0]= 0.8725262656678667
//a[1]= 0.789550775254567
//a[2]= 0.03138225527352545
//a[3]= 0.08669497525356051
//a[4]= 0.9996715282822418
    public static void task4(String[] args) throws MPIException {
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

//command: /usr/bin/mpirun -np 2 java -cp /home/ivan/Documents/program/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/fatieiev/Tasks 2
//OUTPUT: 
// 0
// 0
// 1
// 1    
    public static void task5(String[] args) throws MPIException {
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
                System.out.println(" " + q[i]);
        }
        MPI.Finalize();
    }

    public static void main(String[] args) throws MPIException {
        //task1(args);
        //task2(args);
        //task3(args);
        //task4(args);
        task5(args);
    }

}
