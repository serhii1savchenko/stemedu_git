import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class TestScanModule2 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = 2;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        System.out.println("myrank = " + myrank + ": a = " + Arrays.toString(a));
        int[] q = new int[n];
        MPI.COMM_WORLD.scan(a, q, n, MPI.INT, MPI.SUM);
        for (int j = 0; j < np; j++)
            if (myrank == j) {
                System.out.println("myrank = " + myrank + ": q = " + Arrays.toString(q));
            }
        MPI.Finalize();
    }
}


// javac -cp "/usr/local/lib/mpi.jar" /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2/TestScanModule2.java
// mpirun -np 4 java -cp /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2 TestScanModule2

// Output for 2 processors and n = 4
//    myrank = 0: a = [0, 1, 2, 3]
//    myrank = 1: a = [0, 1, 2, 3]
//    myrank = 0: q = [0, 1, 2, 3]
//    myrank = 1: q = [0, 2, 4, 6]

// Output for 4 processors and n = 2
//    myrank = 1: a = [0, 1]
//    myrank = 0: a = [0, 1]
//    myrank = 0: q = [0, 1]
//    myrank = 1: q = [0, 2]
//    myrank = 3: a = [0, 1]
//    myrank = 2: a = [0, 1]
//    myrank = 2: q = [0, 3]
//    myrank = 3: q = [0, 4]