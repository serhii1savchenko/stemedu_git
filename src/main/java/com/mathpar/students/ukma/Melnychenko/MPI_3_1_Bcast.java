
import java.util.Arrays;
import java.util.Random;
import mpi.*;


public class MPI_3_1_Bcast {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        double[] a = new double[n];
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
            }
            System.out.println("myrank = " + myrank + " : a = "
                    + Arrays.toString(a));
        }
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0)
            System.out.println("myrank = " + myrank + " : a = "
                    + Arrays.toString(a));
        MPI.Finalize();
    }
}


/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_3_1_Bcast.java
mpirun java MPI_3_1_Bcast 3

myrank = 0 : a = [0.03171440417034843, 0.043247841787475094, 0.0980345742841513]
myrank = 1 : a = [0.03171440417034843, 0.043247841787475094, 0.0980345742841513]myrank = 2 : a = [0.03171440417034843, 0.043247841787475094, 0.0980345742841513]

 */
