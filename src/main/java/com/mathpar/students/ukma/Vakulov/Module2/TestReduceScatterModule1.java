import mpi.MPI;
import mpi.MPIException;

public class TestReduceScatterModule1 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        int[] q = new int[n];
        MPI.COMM_WORLD.reduceScatter(a, q, new int[]{2, 2},
                MPI.INT, MPI.SUM);
        if (myrank == 0) {
            for (int i = 0; i < q.length; i++) {
                System.out.println(" " + q[i]);
            }
        }
        MPI.Finalize();
    }
}


// javac -cp "/usr/local/lib/mpi.jar" /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2/TestReduceScatterModule1.java
// mpirun -np 4 java -cp /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2 TestReduceScatterModule1

// Output for 2 processors and n = 4
//    0
//    2
//    0
//    0