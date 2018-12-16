import mpi.MPI;
import mpi.MPIException;

public class TestScatterModule1 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int[] a = new int[2];
        if (myrank == 0) {
            for (int i = 0; i < 2; i++) {
                a[i] = i;
                System.out.println("a = " + a[i] + " ");
            }
            System.out.println("rank = " + myrank);
        }
        int[] q = new int[2];
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatter(a, 1, MPI.INT, q, 1, MPI.INT, 0);
        for (int i = 0; i < q.length; i++)
            System.out.println("myrank = " + myrank + " ; " + q[i] + "\n");
        MPI.Finalize();
    }
}


// javac -cp "/usr/local/lib/mpi.jar" /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2/TestScatterModule1.java
// mpirun -np 4 java -cp /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2 TestScatterModule1

// Output for two processors
//    a = 0
//    a = 1
//    rank = 0
//    myrank = 0 ; 0
//
//    myrank = 0 ; 0
//
//    myrank = 1 ; 1
//
//    myrank = 1 ; 0

