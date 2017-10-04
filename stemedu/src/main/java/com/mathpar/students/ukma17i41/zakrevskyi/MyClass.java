
package com.mathpar.students.ukma17i41.zakrevskyi;

import mpi.MPI;
import mpi.MPIException;

public class MyClass {
    public static void main(String[] args) throws MPIException {

MPI.Init(args);

int myrank = MPI.COMM_WORLD.getRank();
System.out.println("Proc num " + myrank + " Hello World");

MPI.Finalize();
}
    
    

}