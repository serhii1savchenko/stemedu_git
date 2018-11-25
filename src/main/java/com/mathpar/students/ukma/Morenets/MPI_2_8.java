import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class MPI_2_8 {

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);

        var WORLD = MPI.COMM_WORLD;

        int rank = WORLD.getRank();

        int size = Integer.parseInt(args[0]);
        int[] arr = new int[size];
        Arrays.fill(arr, rank);

        int procNum = WORLD.getSize();
        int[] res = new int[size * procNum];

        // How many elements to receive from each proc
        var recvCount = new int[procNum];
        Arrays.fill(recvCount, size);

        var offsets = new int[]{0, 2, 5};

        WORLD.allGatherv(arr, size, MPI.INT, res, recvCount, offsets, MPI.INT);

        Thread.sleep(size * rank);

        System.out.println("Proc " + rank + " received:");

        for (int el : res)
            System.out.println(el);

        System.out.println();

        MPI.Finalize();
    }
}

/*
Command: mpirun -np 3 java -cp out/production/MPI_2_8 MPI_2_8 4

Output:
Proc 0 received:
0
0
1
1
1
2
2
2
2
0
0
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
2
0
0
0

Proc 2 received:
0
0
0
0
1
1
1
1
2
2
2
2
*/
