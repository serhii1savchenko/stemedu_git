import mpi.*;

public class MPI_2_2_HelloWorldParallel {
    public static void main(String[] args)
        throws MPIException {

        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.getRank();
        System.out.println("Proc num " + myrank + " Hello World");

        MPI.Finalize();
    }
}


/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_2_2_HelloWorldParallel.java
mpirun java MPI_2_2_HelloWorldParallel.java
Proc num 0 Hello World
Proc num 1 Hello World
*/