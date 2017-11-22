package com.mathpar.students.ukma17i41.zakrevskyi;

import java.util.Random;
import mpi.*;

/*
 * Закревський Олександр, Output:
a[0]= 0.1134654676359097
a[1]= 0.9100827563445885
a[2]= 0.20852838018098463
a[3]= 0.36046247801557285
a[4]= 0.9953860247216245
a[5]= 0.8492650666162403
a[6]= 0.35924183255022646
a[7]= 0.40371887553480146
a[8]= 0.8493697028088506
a[9]= 0.5407392366685381
Proc num 0 Array sent

a[0]= 0.1134654676359097
a[1]= 0.9100827563445885
a[2]= 0.20852838018098463
a[3]= 0.36046247801557285
a[4]= 0.9953860247216245
a[5]= 0.8492650666162403
a[6]= 0.35924183255022646
a[7]= 0.40371887553480146
a[8]= 0.8493697028088506
a[9]= 0.5407392366685381
Proc num 1 Array received
 */
public class Task4 {
    public static void main(String[] args)
            throws MPIException {
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
}
