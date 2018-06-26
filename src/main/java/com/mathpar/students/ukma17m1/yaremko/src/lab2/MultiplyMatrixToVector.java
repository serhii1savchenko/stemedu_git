/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yaremko.src.lab2;

/**
 *
 * @author Solomka
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
mpirun -n 8 java -cp target/classes/ "com.mathpar.students.ukma17m1.yaremko.src.lab2.MultiplyMatrixToVector" 15 15

I'm processor 5
I'm processor 7
I'm processor 3
I'm processor 6
I'm processor 2
I'm processor 4

I'm processor 1

Matrix A = 
[[4,  19, 21, 12, 27, 26, 18, 22, 7,  16, 6,  16, 16, 3,  31]
 [27, 26, 4,  12, 29, 29, 22, 24, 7,  6,  21, 8,  6,  18, 10]
 [24, 13, 9,  14, 17, 3,  24, 29, 14, 9,  18, 29, 17, 14, 9 ]
 [14, 0,  23, 8,  19, 5,  9,  19, 2,  4,  24, 2,  9,  31, 21]
 [27, 1,  7,  19, 14, 23, 4,  26, 22, 24, 9,  16, 13, 29, 16]
 [4,  15, 23, 29, 28, 19, 28, 16, 1,  6,  19, 11, 10, 10, 28]
 [5,  7,  14, 3,  15, 14, 13, 6,  6,  10, 12, 4,  25, 2,  15]
 [9,  29, 8,  28, 23, 13, 4,  20, 23, 6,  31, 14, 0,  3,  20]
 [20, 6,  21, 19, 3,  7,  6,  24, 30, 18, 15, 12, 18, 25, 23]
 [25, 17, 24, 14, 20, 8,  6,  9,  5,  2,  27, 31, 26, 1,  2 ]
 [4,  14, 27, 1,  6,  20, 18, 6,  5,  7,  23, 20, 22, 22, 1 ]
 [14, 27, 12, 9,  3,  28, 30, 9,  27, 9,  25, 21, 0,  19, 11]
 [25, 16, 8,  15, 20, 2,  15, 31, 20, 23, 4,  21, 18, 3,  21]
 [2,  16, 28, 28, 19, 17, 1,  30, 0,  25, 14, 5,  23, 6,  31]
 [26, 8,  13, 3,  6,  17, 25, 4,  9,  13, 0,  19, 27, 25, 7 ]]
Vector B = [24, 2, 7, 24, 26, 20, 6, 1, 12, 14, 17, 15, 24, 20, 22]
rank = 0 row = [4, 19, 21, 12, 27, 26, 18, 22, 7, 16, 6, 16, 16, 3, 31]
rank = 0 row = [27, 26, 4, 12, 29, 29, 22, 24, 7, 6, 21, 8, 6, 18, 10]
rank = 0 row = [24, 13, 9, 14, 17, 3, 24, 29, 14, 9, 18, 29, 17, 14, 9]
rank = 0 row = [14, 23, 8, 19, 5, 9, 19, 2, 4, 24, 2, 9, 31, 21]
rank = 0 row = [27, 1, 7, 19, 14, 23, 4, 26, 22, 24, 9, 16, 13, 29, 16]
rank = 0 row = [4, 15, 23, 29, 28, 19, 28, 16, 1, 6, 19, 11, 10, 10, 28]
rank = 0 row = [5, 7, 14, 3, 15, 14, 13, 6, 6, 10, 12, 4, 25, 2, 15]
rank = 0 row = [9, 29, 8, 28, 23, 13, 4, 20, 23, 6, 31, 14, 3, 20]
rank = 5row = [25, 16, 8, 15, 20, 2, 15, 31, 20, 23, 4, 21, 18, 3, 21]
rank = 5 B = [24, 2, 7, 24, 26, 20, 6, 1, 12, 14, 17, 15, 24, 20, 22]
rank = 3row = [4, 14, 27, 1, 6, 20, 18, 6, 5, 7, 23, 20, 22, 22, 1]
rank = 3 B = [24, 2, 7, 24, 26, 20, 6, 1, 12, 14, 17, 15, 24, 20, 22]
send result
send result
rank = 2row = [25, 17, 24, 14, 20, 8, 6, 9, 5, 2, 27, 31, 26, 1, 2]
rank = 2 B = [24, 2, 7, 24, 26, 20, 6, 1, 12, 14, 17, 15, 24, 20, 22]
rank = 1row = [20, 6, 21, 19, 3, 7, 6, 24, 30, 18, 15, 12, 18, 25, 23]
rank = 4row = [14, 27, 12, 9, 3, 28, 30, 9, 27, 9, 25, 21, 19, 11]
rank = 1 B = [24, 2, 7, 24, 26, 20, 6, 1, 12, 14, 17, 15, 24, 20, 22]
rank = 6row = [2, 16, 28, 28, 19, 17, 1, 30, 25, 14, 5, 23, 6, 31]rank = 4 B = [24, 2, 7, 24, 26, 20, 6, 1, 12, 14, 17, 15, 24, 20, 22]
send result

rank = 6 B = [24, 2, 7, 24, 26, 20, 6, 1, 12, 14, 17, 15, 24, 20, 22]
rank = 7row = [26, 8, 13, 3, 6, 17, 25, 4, 9, 13, 19, 27, 25, 7]send result

send result
rank = 7 B = [24, 2, 7, 24, 26, 20, 6, 1, 12, 14, 17, 15, 24, 20, 22]
send result
send result
A * B = [null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]

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
