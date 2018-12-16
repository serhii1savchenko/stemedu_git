/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Dymchenko.Module3;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Array;
import com.mathpar.number.Element;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.parallel.utils.MPITransport;
import java.io.IOException;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

public class MultiplyMatrixToVector {
    public static void main(String[] args)throws MPIException, IOException,ClassNotFoundException {
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
        System.out.println("rank = " + rank + " row = "+ Array.toString(A.M[i]));
        }
        for (int j = 1; j < size; j++) {
            for (int z = 0; z < k; z++)
            MPITransport.sendObject(
            A.M[n + (j - 1) * k
            + j * z], j, 100 + j);
            MPITransport.sendObject(B.V, j, 100 + j);
        }
        Element[] result = new Element[ord];
        System.arraycopy(res0, 0, result, 0, n);
        for (int t = 1; t < size; t++) {
        Element[] resRank = (Element[])
        MPITransport.recvObject(t, 100 + t);
        System.arraycopy(resRank, 0, result, n +
        (t - 1) * k, resRank.length);
        }
        System.out.println("A * B = " +
        new VectorS(result).toString(ring));
        } else {
            System.out.println("I'm processor " + rank);
            Element[][] A = new Element[k][ord];
            for (int i = 0; i < k; i++) {
                A[i] = (Element[])
                MPITransport.recvObject(0, 100 + rank);
                System.out.println("rank = " + rank + " row = " + Array.toString(A[i]));
            }
            Element[] B = (Element[])
            MPITransport.recvObject(0, 100 + rank);
            System.out.println("rank = " + rank + " B = "
            + Array.toString(B));
            Element[] result = new Element[k];
            for (int j = 0; j < A.length; j++)
            result[j] = new VectorS(A[j]).multiply(
            new VectorS(B), ring);
            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("send result");
        }
        MPI.Finalize();
    }
}

/*
 mpirun -np 2 java -cp /home/oleksiy/Dymchenko/stemedu/target/classes com.mathpar.students.ukma.Dymchenko.Module3/MultiplyMatrixToVector 4

I'm processor 1
Matrix A = 
[[0,  22, 6,  20]
 [21, 21, 28, 7 ]
 [1,  31, 28, 21]
 [29, 23, 19, 28]]
Vector B = [14, 26, 8, 3]
rank = 0 row = [22, 6, 20]
rank = 0 row = [21, 21, 28, 7]
rank = 1 row = [1, 31, 28, 21]
rank = 1 row = [29, 23, 19, 28]
rank = 1 B = [14, 26, 8, 3]
send result
*/