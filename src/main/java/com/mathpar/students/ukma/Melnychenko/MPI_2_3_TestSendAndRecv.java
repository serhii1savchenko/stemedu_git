import java.util.Random;
import mpi.*;

public class MPI_2_3_TestSendAndRecv {

    public static void main(String[] args) throws MPIException
    {
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
                System.out.println("Proc num " + myrank + " Array sent." + "\n");
            } else {
                MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
                for (int i = 0; i < n; i++) {
                    System.out.println("a[" + i + "]= " + a[i]);
                }
            }
            System.out.println("Proc num " + myrank + " Array received.");
            MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_2_3_TestSendAndRecv.java
mpirun java MPI_2_3_TestSendAndRecv 2

a[0]= 0.8622289271303374
a[1]= 0.24370372065147938
Proc num 0 Array sent.

Proc num 0 Array received.
a[0]= 0.8622289271303374
a[1]= 0.24370372065147938
Proc num 1 Array received.
*/