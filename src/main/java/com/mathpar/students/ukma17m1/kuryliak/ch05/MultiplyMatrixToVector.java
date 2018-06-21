package com.mathpar.students.ukma17m1.kuryliak.ch05;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.*;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class MultiplyMatrixToVector {

    // mpirun -np 4 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch05.MultiplyMatrixToVector 9
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        Ring ring = new Ring("Z[x]");
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        int ord = Integer.parseInt(args[0]);
        int k = ord / size;
        int n = ord - k*(size-1);

        if (rank == 0) {
            int den = 10000;
            Random rnd = new Random();

            MatrixS A = new MatrixS(ord, ord, den, new int[] {5, 5}, rnd, NumberZp32.ONE, ring);
            System.out.println("Matrix A = " + A);
            VectorS B = new VectorS(ord, den, new int[] {5, 5}, rnd, ring);
            System.out.println("Vector B = " + B);

            Element[] res0 = new Element[n];
            for (int i = 0; i < n; i++) {
                Element[] row = A.getRow(i, ring);
                res0[i] = new VectorS(row).multiply(B.V, ring);
                System.out.println("rank = " + rank + " row = " + Arrays.toString(row));
            }

            for (int j = 1; j < size; j++) {
                for (int z = 0; z < k; z++) {
                    MPITransport.sendObject(A.getRow(n + (j-1)*k + z, ring), j, 100 + j);
                }
                MPITransport.sendObject(B.V, j, 100+j);
            }

            Element[] result = new Element[ord];
            System.arraycopy(res0, 0, result, 0, n);

            for (int t = 1; t < size; t++) {
                Element[] resRank = (Element[]) MPITransport.recvObject(t, 100+t);
                System.arraycopy(resRank, 0, result, n + (t-1)*k, resRank.length);
            }

            System.out.println("A * B = " + new VectorS(result).toString(ring));
        } else {
            Element[][] A = new Element[k][ord];
            for (int i = 0; i < k; i++) {
                A[i] = (Element[]) MPITransport.recvObject(0, 100 + rank);
                System.out.println("rank = " + rank + " row = " + Arrays.toString(A[i]));
            }

            Element[] B = (Element[]) MPITransport.recvObject(0, 100 + rank);
            System.out.println("rank = " + rank + " B = " + Arrays.toString(B));

            Element[] result = new Element[k];
            for (int j = 0; j < A.length; j++) {
                result[j] = new VectorS(A[j]).multiply(B, ring);
            }

            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("send result");
        }
        MPI.Finalize();
    }

    /*
    Matrix A =
    [[8,  15, 19, 7,  11, 22, 0,  28, 11]
     [17, 2,  8,  26, 24, 17, 0,  29, 2 ]
     [17, 10, 20, 29, 0,  29, 7,  14, 5 ]
     [26, 18, 10, 21, 10, 20, 20, 7,  25]
     [20, 24, 26, 17, 21, 8,  4,  26, 28]
     [22, 31, 18, 30, 19, 15, 8,  15, 6 ]
     [23, 31, 9,  16, 29, 0,  25, 30, 26]
     [31, 16, 14, 6,  17, 8,  7,  3,  13]
     [21, 19, 8,  1,  15, 31, 17, 24, 28]]
    Vector B = [21, 5, 10, 2, 25, 8, 2, 13, 1]
    rank = 0 row = [8, 15, 19, 7, 11, 22, 0, 28, 11]
    rank = 0 row = [17, 2, 8, 26, 24, 17, 0, 29, 2]
    rank = 0 row = [17, 10, 20, 29, 0, 29, 7, 14, 5]
    rank = 1 row = [26, 18, 10, 21, 10, 20, 20, 7, 25]
    rank = 1 row = [20, 24, 26, 17, 21, 8, 4, 26, 28]
    rank = 1 B = [21, 5, 10, 2, 25, 8, 2, 13, 1]
    rank = 2 row = [22, 31, 18, 30, 19, 15, 8, 15, 6]
    rank = 3 row = [31, 16, 14, 6, 17, 8, 7, 3, 13]
    rank = 2 row = [23, 31, 9, 16, 29, 0, 25, 30, 26]
    rank = 2 B = [21, 5, 10, 2, 25, 8, 2, 13, 1]
    rank = 3 row = [21, 19, 8, 1, 15, 31, 17, 24, 28]
    rank = 3 B = [21, 5, 10, 2, 25, 8, 2, 13, 1]
    send result
    send result
    send result
    A * B = [1273, 1614, 1098, 1344, 1797, 1669, 1951, 1438, 1615]
    */

}
