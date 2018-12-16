import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

import java.util.Random;

public class TestCreateIntracommModule1 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(
                new int[]{0, 1});
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);
        int myrank = COMM_NEW.getRank();
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
        COMM_NEW.barrier();
        COMM_NEW.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0) {
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        MPI.Finalize();
    }
}

// javac -cp "/usr/local/lib/mpi.jar" /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2/TestCreateIntracommModule1.java
// mpirun -np 4 java -cp /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2 TestCreateIntracommModule1

// Output for 2 processors and n=4
//    a[0]= 0.47572440952136663
//    a[1]= 0.995912269136856
//    a[2]= 0.9339344304444064
//    a[3]= 0.7478147283151642
//    a[0]= 0.47572440952136663
//    a[1]= 0.995912269136856
//    a[2]= 0.9339344304444064
//    a[3]= 0.7478147283151642
