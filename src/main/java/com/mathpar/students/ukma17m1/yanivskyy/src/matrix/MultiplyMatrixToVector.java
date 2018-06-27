/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yanivskyy.src.matrix;

/**
 *
 * @author z1kses
 */
import com.mathpar.parallel.utils.MPITransport;
import com.mathpar.number.VectorS;
import com.mathpar.number.Ring;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Element;
import com.mathpar.number.Array;
import mpi.*;
import com.mathpar.matrix.MatrixS;
import java.util.Random;
import java.io.IOException;

/*

mpirun -n 8 java -cp target/classes/ "com.mathpar.students.ukma17m1.yanivskyy.src.matrix.MultiplyMatrixToVector" 25 25

I'm processor 6
I'm processor 1
I'm processor 7
I'm processor 3
I'm processor 5
I'm processor 2
I'm processor 4
Matrix A = 
[[30, 1,  27, 18, 1,  5,  28, 10, 15, 28, 1,  22, 22, 18, 30, 17, 16, 23, 30, 1,  13, 17, 18, 10, 0 ]
 [0,  24, 13, 9,  24, 13, 6,  3,  28, 21, 4,  15, 23, 10, 10, 1,  0,  4,  7,  9,  13, 8,  18, 3,  23]
 [19, 9,  3,  16, 8,  10, 27, 1,  24, 18, 15, 8,  17, 21, 27, 16, 10, 30, 3,  18, 14, 9,  27, 12, 7 ]
 [30, 24, 31, 13, 15, 22, 14, 14, 20, 11, 20, 0,  5,  12, 6,  5,  25, 29, 10, 29, 30, 31, 23, 18, 25]
 [21, 14, 0,  30, 23, 24, 3,  19, 27, 0,  5,  1,  0,  19, 8,  27, 29, 22, 17, 19, 9,  8,  22, 26, 12]
 [24, 9,  23, 5,  8,  4,  10, 29, 4,  23, 3,  12, 28, 16, 29, 10, 9,  10, 0,  25, 30, 25, 22, 17, 13]
 [20, 12, 11, 29, 4,  12, 18, 10, 12, 25, 7,  28, 25, 12, 19, 10, 11, 14, 18, 17, 5,  22, 29, 17, 18]
 [3,  25, 8,  24, 28, 31, 18, 0,  8,  11, 31, 6,  9,  8,  22, 30, 28, 27, 10, 8,  22, 15, 27, 22, 24]
 [1,  15, 26, 20, 13, 16, 21, 4,  24, 8,  29, 7,  31, 28, 14, 30, 0,  6,  21, 7,  7,  22, 25, 24, 27]
 [10, 10, 31, 9,  12, 14, 25, 17, 12, 9,  16, 16, 31, 12, 25, 14, 23, 1,  13, 9,  3,  22, 7,  1,  9 ]
 [8,  31, 5,  1,  18, 5,  29, 21, 22, 0,  25, 23, 11, 8,  26, 2,  7,  2,  0,  21, 15, 27, 19, 11, 28]
 [7,  21, 31, 15, 27, 8,  0,  24, 8,  28, 16, 4,  26, 27, 2,  5,  17, 24, 19, 6,  31, 21, 8,  15, 2 ]
 [19, 10, 21, 2,  0,  30, 5,  13, 28, 9,  18, 18, 12, 3,  29, 28, 2,  26, 5,  17, 12, 9,  15, 10, 28]
 [24, 23, 20, 24, 23, 23, 7,  3,  22, 22, 6,  11, 8,  21, 23, 20, 24, 22, 4,  4,  11, 14, 17, 17, 23]
 [13, 0,  8,  2,  11, 3,  31, 31, 21, 0,  28, 6,  23, 10, 7,  18, 12, 30, 13, 31, 3,  28, 23, 4,  20]
 [8,  10, 15, 4,  1,  0,  31, 21, 0,  7,  6,  18, 19, 23, 31, 24, 27, 26, 13, 8,  3,  22, 21, 12, 3 ]
 [31, 29, 8,  22, 27, 6,  4,  23, 21, 7,  22, 2,  6,  11, 21, 8,  1,  12, 27, 19, 30, 16, 25, 28, 8 ]
 [4,  2,  10, 10, 23, 17, 13, 23, 26, 28, 27, 26, 21, 29, 9,  29, 11, 29, 13, 29, 18, 4,  18, 14, 13]
 [30, 27, 10, 11, 16, 29, 0,  4,  28, 23, 26, 17, 17, 20, 15, 12, 22, 4,  10, 9,  23, 22, 26, 20, 17]
 [25, 28, 12, 26, 20, 6,  31, 21, 3,  21, 7,  21, 20, 24, 4,  15, 2,  30, 26, 4,  16, 29, 13, 14, 9 ]
 [5,  27, 11, 22, 5,  26, 9,  0,  27, 18, 17, 7,  1,  28, 22, 3,  22, 28, 30, 26, 10, 28, 18, 2,  14]
 [11, 3,  25, 27, 20, 30, 1,  2,  2,  17, 0,  20, 19, 29, 24, 18, 27, 31, 3,  23, 28, 25, 18, 4,  3 ]
 [6,  7,  13, 5,  30, 16, 2,  26, 6,  6,  0,  26, 27, 6,  25, 22, 0,  14, 0,  25, 6,  26, 25, 31, 28]
 [0,  0,  21, 23, 10, 25, 19, 3,  19, 25, 22, 10, 4,  18, 1,  4,  16, 4,  22, 0,  14, 1,  16, 17, 22]
 [1,  3,  28, 30, 8,  23, 1,  22, 21, 1,  8,  1,  12, 4,  14, 21, 5,  7,  15, 22, 22, 24, 22, 15, 31]]
Vector B = [0, 3, 18, 20, 7, 27, 17, 16, 10, 22, 28, 23, 26, 5, 28, 12, 24, 3, 16, 21, 14, 30, 7, 11, 17]
rank = 0 row = [30, 1, 27, 18, 1, 5, 28, 10, 15, 28, 1, 22, 22, 18, 30, 17, 16, 23, 30, 1, 13, 17, 18, 10]
rank = 0 row = [24, 13, 9, 24, 13, 6, 3, 28, 21, 4, 15, 23, 10, 10, 1, 4, 7, 9, 13, 8, 18, 3, 23]
rank = 0 row = [19, 9, 3, 16, 8, 10, 27, 1, 24, 18, 15, 8, 17, 21, 27, 16, 10, 30, 3, 18, 14, 9, 27, 12, 7]
rank = 0 row = [30, 24, 31, 13, 15, 22, 14, 14, 20, 11, 20, 5, 12, 6, 5, 25, 29, 10, 29, 30, 31, 23, 18, 25]
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 26
	at com.mathpar.students.ukma17m1.yanivskyy.src.matrix.MultiplyMatrixToVector.main(MultiplyMatrixToVector.java:96)


*/
public class MultiplyMatrixToVector {
    public static void main(String[] args)
            throws MPIException, IOException,
            ClassNotFoundException {
        Ring ring = new Ring("Z[x]");
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        int ord = Integer.parseInt(args[0]);

        int k = ord / size;

        int n = ord - k * (size - 1);
        if (rank == 0) {
            int den = 10000;
            Random rnd = new Random();
            MatrixS A = new MatrixS(ord, ord, den,
                    new int[] {5, 5}, rnd, NumberZp32.ONE, ring);
            System.out.println("Matrix A = " + A);
            VectorS B = new VectorS(ord, den,
                    new int[] {5, 5}, rnd, ring);
            System.out.println("Vector B = " + B);

            Element[] res0 = new Element[n];
            for (int i = 0; i < n; i++) {
                res0[i] = new VectorS(A.M[i]).multiply(B, ring);
                System.out.println("rank = " + rank + " row = "
                        + Array.toString(A.M[i]));
            }

            for (int j = 1; j < size; j++) {
                for (int z = 0; z < k; z++) {
                    MPITransport.sendObject(
                            A.M[n + (j - 1) * k
                            + j * z], j, 100 + j);
                }

                MPITransport.sendObject(B.V, j, 100 + j);
            }

            Element[] result = new Element[ord];
            System.arraycopy(res0, 0, result, 0, n);

            for (int t = 1; t < size; t++) {
                Element[] resRank = (Element[]) MPITransport.recvObject(t, 100 + t);
                System.arraycopy(resRank, 0, result, n
                        + (t - 1) * k, resRank.length);
            }
            System.out.println("A * B = "
                    + new VectorS(result).toString(ring));
        } else {

            System.out.println("I'm processor " + rank);

            Element[][] A = new Element[k][ord];
            for (int i = 0; i < k; i++) {
                A[i] = (Element[]) MPITransport.recvObject(0, 100 + rank);
                System.out.println("rank = " + rank + "row = " + Array.toString(A[i]));
            }
            Element[] B = (Element[]) MPITransport.recvObject(0, 100 + rank);
            System.out.println("rank = " + rank + " B = " + Array.toString(B));

            Element[] result = new Element[k];
            for (int j = 0; j < A.length; j++) {
                result[j] = new VectorS(A[j]).multiply(
                        new VectorS(B), ring);
            }

            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("send result");
        }
        MPI.Finalize();
    }
}
