/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.safonova;
import java.nio.IntBuffer;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

/* 
 @author safonova
 */


public class task1 {
    

    /*
    task 1
    */
    
    
    public static void Task1(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World" + "\n");
        //end of the parallel part
        MPI.Finalize();
    }
    /*     Run: 
       openmpi/bin/mpirun -np 2 java -cp /home/safonova/stemedu/target/classes com/mathpar/students/ukma17i41/safonova/task1 2
    
       Output: Proc num 0 Hello World 
               Proc num 1 Hello World
    
    */
    
    /*
    task 2 
    */

private static void Task2(String[] args)
throws MPIException {

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
System.out.println("proc num = " + myrank +" Array sent");
} else {
MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);
System.out.println("proc num = " + myrank +" Array received");
}
MPI.Finalize();
}  

/*Run: 
  openmpi/bin/mpirun -np 2 java -cp /home/safonova/stemedu/target/classes com/mathpar/students/ukma17i41/safonova/task1 3  
    
Output: 
proc num = 0 Array sent
proc num = 1 Array received
*/

    

    /*
    task 3
    */

  public static void Task3(String[] args) throws MPIException {
        //MPI initialization
        MPI.Init(args);
        //CPU number detection
        int myrank = MPI.COMM_WORLD.getRank();
        //determining the number of processors in a group
        int np = MPI.COMM_WORLD.getSize();
        //array size
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
            System.out.println("proc num = " + myrank
                    + ": Array was sent");
        } else {
            MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);
            System.out.println("proc num = " + myrank
                    + ": Array was accepted");
        }
        //end of the parallel part
        MPI.Finalize();
    }
    
/*Run:
 openmpi/bin/mpirun -np 2 java -cp /home/safonova/stemedu/target/classes com/mathpar/students/ukma17i41/safonova/task1 4
*/
/*Output:
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

/*
    task 4
    */
    
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

/*run: openmpi/bin/mpirun -np 2 java -cp /home/safonova/stemedu/target/classes com/mathpar/students/ukma17i41/safonova/task1 2

*/
/* Output:
    a[0]= 0.5198585156451256
    a[1]= 0.7252504261405832
    a[0]= 0.5198585156451256
    a[1]= 0.7252504261405832
*/
public static void main(String[] args) throws MPIException {
        Task1(args);
        //Task2(args);
        //Task3(args);
        //Task4(args);
        //Task5(args);
    }
}