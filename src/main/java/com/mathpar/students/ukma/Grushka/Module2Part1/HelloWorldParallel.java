import mpi.*;

public class HelloWorldParallel {

    public static void main(String[] args) throws MPIException {
        // MPI definition
        MPI.Init(args);
        // Determine the number of processors
        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");

        MPI.Finalize();
    }

}

// Result (4 processors):
//        Proc num 3 Hello World
//        Proc num 2 Hello World
//        Proc num 1 Hello World
//        Proc num 0 Hello World
