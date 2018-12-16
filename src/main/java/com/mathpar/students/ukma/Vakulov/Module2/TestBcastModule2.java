import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import java.util.Random;

public class TestBcastModule2 {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
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

// javac -cp "/usr/local/lib/mpi.jar" /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2/TestBcastModule2.java
// mpirun -np 4 java -cp /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2 TestBcastModule2

// Output for 4 processors and n = 4
//    myrank = 0 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
//    myrank = 3 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
//    myrank = 2 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
//    myrank = 1 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
