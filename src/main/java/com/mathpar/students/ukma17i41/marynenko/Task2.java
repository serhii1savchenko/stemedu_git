package com.mathpar.students.ukma17i41.marynenko;

/**
 * /users/anja/openmpi/bin/mpirun -np 2 java -cp /Users/anja/Downloads/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/marynenko/Task2 2
 * @author anja
 */
import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;

//Output: 
//a[0]= 0.24617305993981564
//a[1]= 0.8036021445684128
//Proc num 0: Array was sent

//a[0]= 0.24617305993981564
//a[1]= 0.8036021445684128
//Proc num 1: Array was accepteda[0]= 0.24617305993981564
//a[1]= 0.8036021445684128
//Proc num 0: Array was sent

//a[0]= 0.24617305993981564
//a[1]= 0.8036021445684128
//Proc num 1: Array was accepted



public class Task2 {
public static void Task2(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        //determining the number of processors in a group
        int np = MPI.COMM_WORLD.getSize();
        //array size
        int n = Integer.parseInt(args[0]);
        double[] a = new double[n];
        //processors synchronization
        MPI.COMM_WORLD.barrier();
        //if processor with number 0
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = (new Random()).nextDouble();
                System.out.println("a[" + i + "]= " + a[i]);
            }
            //send elements by processor with number 0
            for (int i = 1; i < np; i++) {
                MPI.COMM_WORLD.send(a, n, MPI.DOUBLE, i, 3000);
            }
            System.out.println("Proc num " + myrank
                    + ": Array was sent" + "\n");
        } else {
            //i-th processor receives messages from the processor with number 0
            MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
            System.out.println("Proc num " + myrank
                    + ": Array was accepted" + "\n");
        }
        //end of the parallel part
        MPI.Finalize();
    }  
    public static void main(String[] args) throws MPIException {
        Task2(args);
        }
}
