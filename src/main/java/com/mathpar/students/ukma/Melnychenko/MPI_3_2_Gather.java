import java.util.Arrays;
import mpi.*;

public class MPI_3_2_Gather {
    public static void main(String[] args)
            throws Exception{
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        System.out.println(np);
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for(int i = 0; i < n; i++) a[i] = myrank;
        System.out.println("myrank = " + myrank + " : a = "
                + Arrays.toString(a) + " size: " + np);
        int[] q = new int[n*np];
        MPI.COMM_WORLD.gather(a, n, MPI.INT, q, n, MPI.INT,
                np-1);
        if(myrank == np-1)
            System.out.println("myrank = " + myrank + " : q = "
                    + Arrays.toString(q));
        MPI.Finalize();
    }
}


/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_3_2_Gather.java
mpirun java MPI_3_2_Gather 3

myrank = 2 : a = [2, 2, 2]
myrank = 0 : a = [0, 0, 0]
myrank = 1 : a = [1, 1, 1]
myrank = 2 : q = [0, 0, 0, 1, 1, 1, 2, 2, 2]
 */
