import java.util.Random;
import mpi.*;

public class TestBcast {
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

// Output (2 proc) and n = 4
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