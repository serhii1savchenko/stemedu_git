
package com.mathpar.students.ukma17i41.zakrevskyi;

import mpi.MPI;
import mpi.MPIException;

//Закревський Олександр, HelloWorld 
// Для запуску необхідно відкрити термінал та написати наступну команду:
// openmpi/bin/mpirun -np 2 java -cp /home/sasha/stemedu/stemedu/target/classes  
// com/mathpar/students/ukma17i41/zakrevskyi/Myclass
// де число після - вказує на кількість процесорів, далі шлях до скомпільованих класів
//та власне шлях до класу
// Output: Proc num 0 Hello WorldProc num 1 Hello World


public class HelloWorld {
    public static void main(String[] args) throws MPIException {

MPI.Init(args);

int myrank = MPI.COMM_WORLD.getRank();
System.out.println("Proc num " + myrank + " Hello World");

MPI.Finalize();
}
    
    

}