import mpi.MPI;
import mpi.MPIException;

import java.nio.IntBuffer;

public class TestAllToAllModule1 {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();

        int n = 2;
        if (args.length != 0)
        {
            n = Integer.parseInt(args[0]);
        }
        IntBuffer b = MPI.newIntBuffer(4);
        b.put(0); b.put(1); b.put(2); b.put(3);
        for (int i = 0; i < 4; i++)
            System.out.println("b[" + i + "] = " + b.get(i));
        System.out.println("capacity b = " + b.capacity());
        IntBuffer q = MPI.newIntBuffer(4);
        MPI.COMM_WORLD.allToAll(b, n, MPI.INT, q, n, MPI.INT);
        for (int i = 0; i < q.capacity(); i++) {
            System.out.println("myrank = " + myrank +
                    " send=" + b.get(i) + " recv=" + q.get(i));
        }
        MPI.Finalize();
    }
}


// javac -cp "/usr/local/lib/mpi.jar" /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2/TestAllToAllModule1.java
// mpirun -np 4 java -cp /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2 TestAllToAllModule1

// Output for 2 processors 
//    b[0] = 0
//    b[1] = 1
//    b[2] = 2
//    b[3] = 3
//    capacity b = 4
//    b[0] = 0
//    b[1] = 1
//    b[2] = 2
//    b[3] = 3
//    capacity b = 4
//    myrank = 0 send=0 recv=0
//    myrank = 0 send=1 recv=1
//    myrank = 0 send=2 recv=0
//    myrank = 0 send=3 recv=1
//    myrank = 1 send=0 recv=2
//    myrank = 1 send=1 recv=3
//    myrank = 1 send=2 recv=2
//    myrank = 1 send=3 recv=3

