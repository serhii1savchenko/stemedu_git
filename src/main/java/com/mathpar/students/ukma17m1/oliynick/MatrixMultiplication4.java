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

Processor 3
Processor 2
Processor 1
res = 
[[1.55, 0.4 ]
 [1.18, 0.28]]
res = 
[[0.31, 0.06]
 [2.12, 0.45]]
res = 
[[0.21, 0.39]
 [1.73, 1.5 ]]

recv 1 to 0
recv 2 to 0
recv 3 to 0

Result=
[[1.59, 1.08, 1.55, 0.4 ]
 [1.05, 0.98, 1.18, 0.28]
 [0.21, 0.39, 0.31, 0.06]
 [1.73, 1.5,  2.12, 0.45]]
*/

public final class MatrixMultiplication4 {

    private static final int MOD = 13;

    private static MatrixS mmultiply(MatrixS a, MatrixS b, MatrixS c, MatrixS d, Ring ring) {
        return (a.multiply(b, ring)).add(c.multiply(d, ring), ring);
    }

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {

        final Ring ring = new Ring("R64[x]");

        MPI.Init(new String[0]);

        final int rank = MPI.COMM_WORLD.getRank();

        if (rank == 0) {

            ring.setMOD32(MOD);

            final int ord = 4;
            final int den = 10000;
            final Random rnd = new Random();
            final MatrixS A = new MatrixS(ord, ord, den, new int[]{5, 5}, rnd, NumberZp32.ONE, ring);
            final MatrixS B = new MatrixS(ord, ord, den, new int[]{5, 5}, rnd, NumberZp32.ONE, ring);
            final MatrixS[] DD = new MatrixS[4];
            final MatrixS[] AA = A.split();
            final MatrixS[] BB = B.split();

            MPITransport.sendObjectArray(new Object[]{AA[0], BB[1], AA[1], BB[3]}, 0, 4, 1, 1);

            MPITransport.sendObjectArray(new Object[]{AA[2], BB[0], AA[3], BB[2]}, 0, 4, 2, 2);

            MPITransport.sendObjectArray(new Object[]{AA[2], BB[1], AA[3], BB[3]}, 0, 4, 3, 3);

            DD[0] = (AA[0].multiply(BB[0], ring)).add(AA[1].multiply(BB[2], ring), ring);

            DD[1] = (MatrixS) MPITransport.recvObject(1, 1);
            System.out.println("recv 1 to 0");

            DD[2] = (MatrixS) MPITransport.recvObject(2, 2);
            System.out.println("recv 2 to 0");

            DD[3] = (MatrixS) MPITransport.recvObject(3, 3);
            System.out.println("recv 3 to 0");

            final MatrixS CC = MatrixS.join(DD);
            System.out.println(String.format("Result = %s", CC.toString()));
        } else {

            System.out.println(String.format("Processor %d", rank));

            ring.setMOD32(MOD);

            final Object[] n = new Object[4];

            MPITransport.recvObjectArray(n, 0, 4, 0, rank);

            final MatrixS a = (MatrixS) n[0];
            final MatrixS b = (MatrixS) n[1];
            final MatrixS c = (MatrixS) n[2];
            final MatrixS d = (MatrixS) n[3];

            final MatrixS res = mmultiply(a, b, c, d, ring);

            System.out.println(String.format("res = %s", res));

            MPITransport.sendObject(res, 0, rank);

            System.out.println("Result sent");
        }

        MPI.Finalize();
    }
}
