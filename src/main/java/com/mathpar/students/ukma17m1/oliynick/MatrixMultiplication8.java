package com.mathpar.students.ukma17m1.oliynick;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Random;

// cd ~/IdeaProjects/stemedu/out/production/com/mathpar/students/ukma17m1/oliynick
// export PATH="$PATH:/home/$USER/openmpi-build/bin"
// export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/home/$USER/openmpi-build/lib/"

/*

Processor 6
Processor 5
Processor 4
Processor 2
Processor 3
Processor 7
Processor 1

A = 
[[0.55, 0.32, 0.49, 0.13]
[0.35, 0.92, 0.04, 0.63]
[0.1,  0.32, 0.96, 0.94]
[0.5,  0.15, 0.37, 0.04]]

B =
[[0.31, 0.7,  0.7,  0.35]
[0.79, 0.33, 0.79, 0.4 ]
[0.57, 0.23, 0.15, 0.42]
[0.39, 0.63, 0.96, 0.13]]

Result =
[[0.75, 0.68, 0.83, 0.55]
[1.1,  0.95, 1.58, 0.59]
[1.2,  0.99, 1.36, 0.69]
[0.5,  0.51, 0.56, 0.4 ]]

 */
public final class MatrixMultiplication8 {

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {

        final Ring ring = new Ring("R64[x]");

        MPI.Init(new String[0]);

        final int rank = MPI.COMM_WORLD.getRank();

        if (rank == 0) {
            final int ord = 4;
            final int den = 10000;

            final Random rnd = new Random();
            final MatrixS A = new MatrixS(ord, ord, den, new int[]{5, 3}, rnd, NumberZp32.ONE, ring);

            System.out.println(String.format("A = %s", A));

            final MatrixS B = new MatrixS(ord, ord, den, new int[]{5, 3}, rnd, NumberZp32.ONE, ring);

            System.out.println(String.format("B = %s", B));

            final MatrixS[] AA = A.split();
            final MatrixS[] BB = B.split();
            final int tag = 0;

            MPITransport.sendObjectArray(new Object[]{AA[1], BB[2]}, 0, 2, 1, tag);
            MPITransport.sendObjectArray(new Object[]{AA[0], BB[1]}, 0, 2, 2, tag);
            MPITransport.sendObjectArray(new Object[]{AA[1], BB[3]}, 0, 2, 3, tag);
            MPITransport.sendObjectArray(new Object[]{AA[2], BB[0]}, 0, 2, 4, tag);
            MPITransport.sendObjectArray(new Object[]{AA[3], BB[2]}, 0, 2, 5, tag);
            MPITransport.sendObjectArray(new Object[]{AA[2], BB[1]}, 0, 2, 6, tag);
            MPITransport.sendObjectArray(new Object[]{AA[3], BB[3]}, 0, 2, 7, tag);

            final MatrixS[] DD = new MatrixS[4];

            DD[0] = (AA[0].multiply(BB[0], ring)).add((MatrixS) MPITransport.recvObject(1, 3), ring);
            DD[1] = (MatrixS) MPITransport.recvObject(2, 3);
            DD[2] = (MatrixS) MPITransport.recvObject(4, 3);
            DD[3] = (MatrixS) MPITransport.recvObject(6, 3);

            final MatrixS D = MatrixS.join(DD);

            System.out.println(String.format("Result = %s", D.toString()));
        } else {

            System.out.println(String.format("Processor %d", rank));
            final Object[] b = new Object[2];

            MPITransport.recvObjectArray(b, 0, 2, 0, 0);

            final MatrixS[] a = new MatrixS[b.length];

            for (int i = 0; i < b.length; i++) {
                a[i] = (MatrixS) b[i];
            }

            final MatrixS res = a[0].multiply(a[1], ring);

            if (rank % 2 == 0) {
                MPITransport.sendObject(res.add((MatrixS) MPITransport.recvObject(rank + 1, 3), ring), 0, 3);
            } else {
                MPITransport.sendObject(res, rank - 1, 3);
            }
        }

        MPI.Finalize();
    }

}