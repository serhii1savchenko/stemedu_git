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

MultiplyMatrixToVector

I'm processor 4
I'm processor 6
I'm processor 5
I'm processor 1I'm processor 3I'm processor 2

I'm processor 7

Matrix A = 
[[25, 18, 17, 13, 21]
 [10, 23, 1,  20, 25]
 [29, 18, 26, 19, 7 ]
 [18, 29, 7,  22, 16]
 [3,  15, 30, 3,  30]]
Vector B = [22, 4, 16, 8, 11]
rank = 0 row = [25, 18, 17, 13, 21]
rank = 0 row = [10, 23, 1, 20, 25]
rank = 0 row = [29, 18, 26, 19, 7]
rank = 0 row = [18, 29, 7, 22, 16]
rank = 0 row = [3, 15, 30, 3, 30]
rank = 3 B = [22, 4, 16, 8, 11]rank = 4 B = [22, 4, 16, 8, 11]

send result
rank = 2 B = [22, 4, 16, 8, 11]
send result
rank = 6 B = [22, 4, 16, 8, 11]
send result
rank = 5 B = [22, 4, 16, 8, 11]
send result
rank = 7 B = [22, 4, 16, 8, 11]
rank = 1 B = [22, 4, 16, 8, 11]
send result
send result
send result
A * B = [null, null, null, null, null]

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
