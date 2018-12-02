import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class MPI_2_9_TestAllGatherv {

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();

        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];
        Arrays.fill(arr, rank);

        int procNum = MPI.COMM_WORLD.getSize();
        int[] res = new int[size * procNum];

        // How many elements to receive from each proc
        int[] recvCount = new int[procNum];
        Arrays.fill(recvCount, size);

        int[] offsets = new int[]{0, 2, 5};

        MPI.COMM_WORLD.allGatherv(arr, size, MPI.INT, res, recvCount, offsets, MPI.INT);

        Thread.sleep(size * rank);

        System.out.println("Proc " + rank + " received:");

        for (int el : res)
            System.out.println(el);

        System.out.println();

        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_2_9_TestAllGatherv.java
mpirun java MPI_2_9_TestAllGatherv 3

Proc 0 received:
0
0
1
1
1
2
2
2
0

Proc 1 received:
0
0
1
1
1
2
2
2
0

Proc 2 received:
0
0
1
1
1
2
2
2
0

*/
