import mpi.MPI;
import mpi.MPIException;

public class HelloWorldParallelModule1 {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        MPI.Finalize();
    }

}

// javac -cp "/usr/local/lib/mpi.jar" /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2/HelloWorldParallelModule1.java
// mpirun -np 4 java -cp /Users/ivanvakulov/Desktop/NAUKMA/stemedu/src/main/java/com/mathpar/students/ukma/Vakulov/Module2 HelloWorldParallelModule1

// Output for 4 processors
//   Proc num 1 Hello World
//   Proc num 2 Hello World
//   Proc num 0 Hello World
//   Proc num 3 Hello World
