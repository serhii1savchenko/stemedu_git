import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class TestReduceScatter {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int[] a = new int[n * np];
        for (int i = 0; i < n; i++) a[i] = i;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n];
        MPI.COMM_WORLD.reduceScatter(a, q, new int[]{n, n}, MPI.INT, MPI.SUM);
        if (myrank == 0)
            System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}

//        OUTPUT:
//        myrank = 0: a = [0, 1, 2, 3, 0, 0, 0, 0]
//        myrank = 1: a = [0, 1, 2, 3, 0, 0, 0, 0]
//        myrank = 0: q = [0, 2, 4, 6]