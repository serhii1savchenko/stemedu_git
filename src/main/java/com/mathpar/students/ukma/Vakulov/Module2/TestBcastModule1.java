import mpi.MPI;
import mpi.MPIException;

import java.util.Random;

public class TestBcastModule1 {
    public static void main(String[] args) throws MPIException {
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
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0) {
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        MPI.Finalize();
    }
}

// javac -cp "/usr/local/lib/mpi.jar" /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2/TestBcastModule1.java
// mpirun -np 4 java -cp /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2 TestBcastModule1

// Output for 4 processors and n = 4
//    a[0]= 0.44425017395071087
//    a[1]= 0.7786018081759669
//    a[2]= 0.7845693804372024
//    a[3]= 0.12285034656396099
//    a[0]= 0.44425017395071087
//    a[1]= 0.7786018081759669
//    a[2]= 0.7845693804372024
//    a[3]= 0.12285034656396099
//    a[0]= 0.44425017395071087
//    a[1]= 0.7786018081759669
//    a[2]= 0.7845693804372024
//    a[3]= 0.12285034656396099
//    a[0]= 0.44425017395071087
//    a[1]= 0.7786018081759669
//    a[2]= 0.7845693804372024
//    a[3]= 0.12285034656396099