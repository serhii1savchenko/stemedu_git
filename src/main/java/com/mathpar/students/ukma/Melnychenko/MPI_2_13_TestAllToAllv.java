import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;

public class MPI_2_13_TestAllToAllv {

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        Integer[] arr = new Integer[size];

        for (int i = 0; i < size; ++i)
        {
            arr[i] = i;
            System.out.println("rank = " + rank + "; arr[" + i + "] = " + arr[i]);
        }

        System.out.println();

        Integer[] res = new Integer[size];

        int[] sendSizes = new int[size];
        int[] recvSizes = new int[size];

        Arrays.fill(sendSizes, 1);
        Arrays.fill(recvSizes, 1);

        int[] sendOffsets = new int[size];
        int[] recvOffsets = new int[size];

        for (int i = 0; i < size; i++)
        {
            sendOffsets[i] = i;
            recvOffsets[i] = i;
        }

        MPI.COMM_WORLD.allToAllv(arr, sendSizes, sendOffsets, MPI.INT, res,recvSizes, recvOffsets, MPI.INT);

        Thread.sleep(size * rank);

        for (int i = 0; i < size; i++)
            System.out.println("rank = " + rank + "; send = " + arr[i] + "; recv = " + res[i]);

        System.out.println();

        MPI.Finalize();
    }
}

/*
mpijavac -cp "/usr/local/lib/mpi.jar" MPI_2_13_TestAllToAllv.java
mpirun java MPI_2_13_TestAllToAllv

rank = 1; arr[0] = 0
rank = 1; arr[1] = 1
rank = 3; arr[0] = 0rank = 1; arr[2] = 2

rank = 1; arr[3] = 3
rank = 2; arr[0] = 0
rank = 0; arr[0] = 0
rank = 3; arr[1] = 1

rank = 0; arr[1] = 1rank = 2; arr[1] = 1rank = 3; arr[2] = 2


rank = 3; arr[3] = 3
rank = 0; arr[2] = 2
rank = 2; arr[2] = 2

rank = 2; arr[3] = 3rank = 0; arr[3] = 3



rank = 0; send = 0; recv = 0
rank = 0; send = 1; recv = 0
rank = 0; send = 2; recv = 0
rank = 0; send = 3; recv = 0

rank = 1; send = 0; recv = 1
rank = 1; send = 1; recv = 1
rank = 1; send = 2; recv = 1
rank = 1; send = 3; recv = 1

rank = 2; send = 0; recv = 2
rank = 2; send = 1; recv = 2
rank = 2; send = 2; recv = 2
rank = 2; send = 3; recv = 2

rank = 3; send = 0; recv = 3
rank = 3; send = 1; recv = 3
rank = 3; send = 2; recv = 3
rank = 3; send = 3; recv = 3

*/
