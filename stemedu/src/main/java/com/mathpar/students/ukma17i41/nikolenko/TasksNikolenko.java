package com.mathpar.students.ukma17i41.nikolenko;

import java.nio.IntBuffer;
import mpi.*;
import java.util.Arrays;
import java.util.Random;
import mpi.Intracomm;

public class TasksNikolenko {

    /*
    OUTPUT:
        Proc num 1 Hello World
        Proc num 0 Hello World
     */
    private static void FirstTask(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        MPI.Finalize();
    }

    /*
    OUTPUT:
        a[0]= 0.8463645256018087
        a[1]= 0.7358346181790533
        a[2]= 0.905082929241393
        a[3]= 0.802037956595714
        a[4]= 0.060977621089168044
        a[5]= 0.03885913826393994
        a[6]= 0.9542960195619137
        a[7]= 0.6973246988504471
        a[8]= 0.7171238542766879
        a[9]= 0.6278257445766819
        Proc num 0 Array sent

        a[0]= 0.8463645256018087
        a[1]= 0.7358346181790533
        a[2]= 0.905082929241393
        a[3]= 0.802037956595714
        a[4]= 0.060977621089168044
        a[5]= 0.03885913826393994
        a[6]= 0.9542960195619137
        a[7]= 0.6973246988504471
        a[8]= 0.7171238542766879
        a[9]= 0.6278257445766819
        Proc num 1 Array received
     */
    public static void SecondTask(String[] args) throws MPIException {
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
            System.out.println("Proc num " + myrank + " Array sent" + "\n");
        } else {
            MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
            System.out.println("Proc num " + myrank + " Array received" + "\n");
        }
        MPI.Finalize();
    }

    /*
    OUTPUT:
        proc num = 0 array send
        proc num = 1 array received
     */
    public static void ThirdTask(String[] args) throws MPIException {
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
            System.out.println("proc num = " + myrank + " array send");
        } else {
            MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);
            System.out.println("proc num = " + myrank + " array received");
        }
        MPI.Finalize();
    }

    /*
    OUTPUT:
a[0]= 0.9111548468413283
a[1]= 0.9752855183746622
a[2]= 0.13665367251205363
a[3]= 0.622959926030012
a[4]= 0.9313977544070471
a[5]= 0.5797056128809734
a[6]= 0.9559677803250995
a[7]= 0.9180142832427142
a[8]= 0.9502694160288505
a[9]= 0.5325700392517714
a[0]= 0.9111548468413283
a[1]= 0.9752855183746622
a[2]= 0.13665367251205363
a[3]= 0.622959926030012
a[4]= 0.9313977544070471
a[5]= 0.5797056128809734
a[6]= 0.9559677803250995
a[7]= 0.9180142832427142
a[8]= 0.9502694160288505
a[9]= 0.5325700392517714
     */
    public static void FourthTask(String[] args) throws MPIException {
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

    /*
    OUTPUT:
 0
 0
 0
 0
 0
 0
 0
 0
 0
 0
 1
 1
 1
 1
 1
 1
 1
 1
 1
 1
     */
    public static void FifthTask(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = myrank;
        }
        int[] q = new int[n * np];
        MPI.COMM_WORLD.gather(a, n, MPI.INT, q, n, MPI.INT, np - 1);
        if (myrank == np - 1) {
            for (int i = 0; i < q.length; i++) {
                System.out.println(" " + q[i]);
            }
        }
        MPI.Finalize();
    }

    public static void main(String[] args) throws MPIException {
        FirstTask(args);
        //SecondTask(args);
        //ThirdTask(args);
        //FourthTask(args);
        //FifthTask(args);
    }
}
