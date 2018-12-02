import mpi.MPI;
import mpi.MPIException;


public class MPI_2_14_TestReduce {

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);


        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        int[] arr = new int[size];

        for (int i = 0; i < size; ++i)
        {
            arr[i] = i;
            System.out.println("rank = " + rank + "; arr[" + i + "] = " + arr[i]);
        }

        System.out.println();

        int[] res = new int[size];

        MPI.COMM_WORLD.reduce(arr, res, size, MPI.INT, MPI.SUM, 0);

        Thread.sleep(size * rank);

        if (rank == 0) {
            for (int i = 0; i < res.length; i++)
                System.out.println(res[i]);
        }

        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_2_14_TestReduce.java
mpirun java MPI_2_14_TestReduce

rank = 0; arr[0] = 0
rank = 0; arr[1] = 1
rank = 0; arr[2] = 2
rank = 0; arr[3] = 3

0
4
8
12
rank = 1; arr[0] = 0
rank = 1; arr[1] = 1
rank = 1; arr[2] = 2
rank = 1; arr[3] = 3

rank = 2; arr[0] = 0
rank = 2; arr[1] = 1
rank = 2; arr[2] = 2
rank = 2; arr[3] = 3

rank = 3; arr[0] = 0
rank = 3; arr[1] = 1
rank = 3; arr[2] = 2
rank = 3; arr[3] = 3
*/
