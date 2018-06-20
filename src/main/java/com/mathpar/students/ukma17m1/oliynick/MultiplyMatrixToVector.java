package com.mathpar.students.ukma17m1.oliynick;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.*;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Random;

// cd ~/IdeaProjects/stemedu/out/production/com/mathpar/students/ukma17m1/oliynick
// export PATH="$PATH:/home/$USER/openmpi-build/bin"
// export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/home/$USER/openmpi-build/lib/"

/*

I'm processor 2
I'm processor 3
I'm processor 1

Matrix A =
[[4,  28, 3,  13]
[13, 0,  25, 3 ]
[23, 9,  28, 15]
[7,  3,  2,  2 ]]

Vector B = [4, 16, 7, 6]

rank = 0 row = [4, 28, 3, 13]
rank = 3 row = [7, 3, 2, 2]
rank = 3 B = [4, 16, 7, 6]

send result

rank = 2 row = [23, 9, 28, 15]
rank = 1 row = [13, 25, 3]
rank = 1 B = [4, 16, 7, 6]
rank = 2 B = [4, 16, 7, 6]

send result
send result

A * B =

[null, [[52, 208, 91, 78],
[100, 400, 175, 150],
[12, 48, 21, 18]], [[92, 368, 161, 138],
[36, 144, 63, 54],
[112, 448, 196, 168],
[60, 240, 105, 90]], [[28, 112, 49, 42],
[12, 48, 21, 18],
[8, 32, 14, 12],
[8, 32, 14, 12]]]
*/
public class MultiplyMatrixToVector {

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {

        final Ring ring = new Ring("Z[x]");

        MPI.Init(args);

        final int rank = MPI.COMM_WORLD.getRank();
        final int size = MPI.COMM_WORLD.getSize();
        final int ord = Integer.parseInt(args[0]);
        final int k = ord / size;
        final int n = ord - k * (size - 1);

        if (rank == 0) {
            final int den = 10000;
            final Random rnd = new Random();
            final MatrixS A = new MatrixS(ord, ord, den, new int[]{5, 5}, rnd, NumberZp32.ONE, ring);

            System.out.println(String.format("Matrix A = %s", A));

            final VectorS B = new VectorS(ord, den, new int[]{5, 5}, rnd, ring);

            System.out.println(String.format("Vector B = %s", B));

            final Element[] res0 = new Element[n];

            for (int i = 0; i < n; i++) {
                res0[i] = new VectorS(A.M[i]).multiply(B, ring);
                System.out.println(String.format("rank = %d, row = %s", rank, Array.toString(A.M[i])));
            }
            for (int j = 1; j < size; j++) {
                for (int z = 0; z < k; z++) {
                    MPITransport.sendObject(A.M[n + (j - 1) * k + j * z], j, 100 + j);
                }

                MPITransport.sendObject(B.V, j, 100 + j);
            }

            final Element[] result = new Element[ord];

            System.arraycopy(res0, 0, result, 0, n);

            for (int t = 1; t < size; t++) {
                final Element[] resRank = (Element[]) MPITransport.recvObject(t, 100 + t);
                System.arraycopy(resRank, 0, result, n + (t - 1) * k, resRank.length);
            }

            System.out.println(String.format("A * B = %s", new VectorS(result).toString(ring)));

        } else {
            System.out.println(String.format("I'm processor %d", rank));

            final Element[][] A = new Element[k][ord];

            for (int i = 0; i < k; i++) {
                A[i] = (Element[]) MPITransport.recvObject(0, 100 + rank);

                System.out.println(String.format("rank = %d, row = %s", rank, Array.toString(A[i])));
            }
            final Element[] B = (Element[]) MPITransport.recvObject(0, 100 + rank);

            System.out.println(String.format("rank = %d, row = %s", rank, Array.toString(B)));

            final Element[] result = new Element[k];

            for (int j = 0; j < A.length; j++) {
                result[j] = new VectorS(A[j]).transpose(ring).multiply(new VectorS(B), ring);
            }
            MPITransport.sendObject(result, 0, 100 + rank);

            System.out.println("send result");
        }
        MPI.Finalize();
    }
}