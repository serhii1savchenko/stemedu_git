
package com.mathpar.students.ukma17i41.nedilska;

import mpi.MPI;
import mpi.MPIException;

 /**
 * @author nedilska
 */
 
 
/* TASK 3
	Run: 
	openmpi/bin/mpirun -np 2 java -cp /home/nedilska/stemedu/target/classes com/mathpar/students/ukma17i41/nedilska/Task5 2
*/

public class Task5 {
    public static void Task5(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        //determining the number of processors in a group
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
        //end of the parallel part
        MPI.Finalize();
    }
    public static void main(String[] args) throws MPIException {
        Task5(args);
    }
	
	/* Output:
	 0
	 0
	 1
	 1 */
}