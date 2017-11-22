/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.kravchenko;
import mpi.*;
import java.util.Random;
import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.IntBuffer;
/**
 *
 * @author kravchenko
 */


public class Task1 {
    /*run:
    openmpi/bin/mpirun -np 2 java -cp /home/kravchenko/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task1 2
    */
    
    private static void First_Task(String[] args)
    throws MPIException{
        
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World" + "\n");
        
        MPI.Finalize();
}
   /*output:
    Proc num 0 Hello World
    Proc num 1 Hello World
    */
    
    
    private static void Second_Task(String[] args)
    throws MPIException{
        
/*run:
openmpi/bin/mpirun -np 2 java -cp /home/kravchenko/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task1 2
*/
        
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
            
            System.out.println("Proc num " + myrank +" Array send " + "\n");
        
        } else {
            
            MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);            
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
            
            System.out.println("Proc num " + myrank +" Array recieve " + "\n");
        }
        
        MPI.Finalize();
    }
    /*output:
a[0]= 0.6676818264326652
a[1]= 0.186757648411091
Proc num 0 Array send 

a[0]= 0.6676818264326652
a[1]= 0.186757648411091
Proc num 1 Array recieve 
 */
    
    
private static void Third_Task(String[] args)
    throws MPIException{
    /*
    run:
    openmpi/bin/mpirun -np 2 java -cp /home/kravchenko/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task1 4
    */
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

/*output:
a[0]= 0.3448366608798743
a[1]= 0.46823400682269667
a[2]= 0.5198871103992656
a[3]= 0.3824905966369555
a[0]= 0.3448366608798743
a[1]= 0.46823400682269667
a[2]= 0.5198871103992656
a[3]= 0.3824905966369555
 */

private static void Fourth_Task(String[] args)
    throws MPIException{
    
    /*
    run:
    openmpi/bin/mpirun -np 2 java -cp /home/kravchenko/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task1 4
    */
    
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

/*output:
 0
 0
 0
 0
 1
 1
 1
 1

*/

private static void Fifth_Task(String[] args)
    throws MPIException{
    /*
    run:
    openmpi/bin/mpirun -np 2 java -cp /home/kravchenko/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task1 2
    */
    
    MPI.Init(args);
    
    int myrank = MPI.COMM_WORLD.getRank();
    
    int np = MPI.COMM_WORLD.getSize();
    
    int n = Integer.parseInt(args[0]);
    int[] a = new int[n];
    
    for (int i = 0; i < n; i++) {
        a[i] = myrank;
    }
    
    int[] q = new int[n * np];
    MPI.COMM_WORLD.gatherv(a, n, MPI.INT, q,
            new int[]{n, n}, new int[]{0, 2},
            MPI.INT, np - 1);
    
    if (myrank == np - 1) {
        for (int i = 0; i < q.length; i++) {
            System.out.println(" " + q[i]);
        }
    }
}
    /*
output:
 0
 0
 1
 1
*/

    public static void main(String[] args)
            throws MPIException{
        
        First_Task(args);
        //Second_Task(args);
        //Third_Task(args);
        //Fourth_Task(args);
        //Fifth_Task(args);        

}
}
    
