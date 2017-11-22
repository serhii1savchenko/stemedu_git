
package com.mathpar.students.ukma17i41.marynenko;

import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

/**
 * /users/anja/openmpi/bin/mpirun -np 2 java -cp /Users/anja/Downloads/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/marynenko/Task4 2
 * @author anja
 */

// Output:
// a[0]= 0.4459525165843199
// a[1]= 0.7074304486115639
// a[0]= 0.4459525165843199
// a[1]= 0.7074304486115639

public class Task4 {
    public static void Task4(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
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
        //send data from 0-th processor to others
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0) {
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        //end of the parallel part
        MPI.Finalize();
    }
    public static void main(String[] args) throws MPIException {
        Task4(args);
    }
}
