import mpi.MPI;
import mpi.MPIException;

public class TestScattervModule1 {

        public static void main(String[] args) throws MPIException {
            MPI.Init(args);
            int myrank = MPI.COMM_WORLD.getRank();
            int n = 4;
            if (args.length != 0)
            {
                n = Integer.parseInt(args[0]);
            }
            int[] a = new int[n];
            if (myrank == 0) {
                for (int i = 0; i < a.length; i++)
                    a[i] = i;
            }
            int[] q = new int[n];
            MPI.COMM_WORLD.barrier();
            MPI.COMM_WORLD.scatterv(a, new int[]{3, 2, 1, 1},
                    new int[]{0, 1, 2, 0}, MPI.INT, q, n, MPI.INT, 0);
            for (int i = 0; i < q.length; i++)
                System.out.print("myrank = " + myrank
                        + "; " + q[i] + "\n");
            MPI.Finalize();
        }
}


// javac -cp "/usr/local/lib/mpi.jar" /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2/TestScattervModule1.java
// mpirun -np 4 java -cp /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2 TestScattervModule1

// Output for 2 processors and n = 4
//    myrank = 0; 0
//    myrank = 0; 1
//    myrank = 0; 2
//    myrank = 0; 0
//    myrank = 1; 1
//    myrank = 1; 2
//    myrank = 1; 11958677
//    myrank = 1; 0
