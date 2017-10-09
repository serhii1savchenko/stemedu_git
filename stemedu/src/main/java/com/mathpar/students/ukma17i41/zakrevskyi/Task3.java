package com.mathpar.students.ukma17i41.zakrevskyi;

import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

// Закревський Олександр, Task 2.3
// Для запуску необхідно відкрити термінал та написати наступну команду:
// openmpi/bin/mpirun -np 2 java -cp /home/sasha/stemedu/stemedu/target/classes  
// com/mathpar/students/ukma17i41/zakrevskyi/Task3 та розмір масиву
// де число після -np вказує на кількість процесорів, далі шлях до скомпільованих класів
// та власне шлях до класу
// Output: 
/*

a[0]= 0.9490626695488533
a[1]= 0.5705887070084696
a[2]= 0.837247322141653
a[3]= 0.7339406912777647
a[4]= 0.6331362870525972
a[5]= 0.5741337669931463
a[6]= 0.6921352765984184
a[7]= 0.9702700992269249
a[8]= 0.5990109716542431
a[9]= 0.7042369750250995
Proc num 0 Array sent

a[0]= 0.9490626695488533
a[1]= 0.5705887070084696
a[2]= 0.837247322141653
a[3]= 0.7339406912777647
a[4]= 0.6331362870525972
a[5]= 0.5741337669931463
a[6]= 0.6921352765984184
a[7]= 0.9702700992269249
a[8]= 0.5990109716542431
a[9]= 0.7042369750250995
Proc num 1 Array received


*/

public class Task3{
    public static void main(String[] args)
throws MPIException {

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
System.out.println("Proc num " + myrank +
" Array sent" + "\n");
} else {
MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
for (int i = 0; i < n; i++) {
System.out.println("a[" + i + "]= " + a[i]);
}
System.out.println("Proc num " + myrank +
" Array received" + "\n");
}
MPI.Finalize();

}
}
