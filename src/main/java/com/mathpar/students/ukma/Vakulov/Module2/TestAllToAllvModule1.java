import mpi.MPI;
import mpi.MPIException;

public class TestAllToAllvModule1 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = 4;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.valueOf(i);
        }
        Integer[] b = new Integer[n];
        MPI.COMM_WORLD.allToAllv(a, new int[]{2, 2},
                new int[]{0, 2}, MPI.INT, b, new int[]{2, 2},
                new int[]{0, 2}, MPI.INT);
        for (int i = 0; i < 2; i++) {
            System.out.print("myrank = " + myrank
                    + "; " + b[i] + "\n");
        }
        MPI.Finalize();
    }
}

// javac -cp "/usr/local/lib/mpi.jar" /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2/TestAllToAllvModule1.java
// mpirun -np 4 java -cp /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2 TestAllToAllvModule1

// Output for 2 processors
//    myrank = 0; 0
//    myrank = 0; 1
//    myrank = 1; 2
//    myrank = 1; 3