import mpi.*;

public class HelloWorldParallel {

    public static void main(String[] args) throws MPIException {
        // Initialize a parallel part
        MPI.Init(args);
        // Define a number of a process
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");
        // Completion a parallel part
        MPI.Finalize();
    }

}

// OUTPUT:
// Proc num 1 Hello World
// Proc num 2 Hello World
// Proc num 0 Hello World
// Proc num 3 Hello World
