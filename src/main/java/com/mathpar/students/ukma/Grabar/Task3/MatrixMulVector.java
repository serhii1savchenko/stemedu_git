package com.mathpar.students.ukma.Grabar.Task3;

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

public class MatrixMulVector 
{   
    public static void main(String[] args)
    throws MPIException, IOException, ClassNotFoundException {
        Ring ring = new Ring("R64[x]");
        MPI.Init(new String[0]);
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
                System.out.println("Rank = " + rank + "; Row = "
                    + Array.toString(A.M[i])+ ";");
            }
            for (int j = 1; j < size; j++) {
                for (int z = 0; z < k; z++)
                    MPITransport.sendObject(A.M[n + (j - 1) * k + j * z], j, 100 + j);
                MPITransport.sendObject(B.V, j, 100 + j);
            }
            Element[] result = new Element[ord];
            System.arraycopy(res0, 0, result, 0, n);
            for (int t = 1; t < size; t++) {
                Element[] resRank = (Element[]) MPITransport.recvObject(t, 100 + t);
                System.arraycopy(resRank, 0, result, n + (t - 1) * k, resRank.length);
            }
            System.out.println("A * B = " + new VectorS(result).toString(ring));
        } else {
           System.out.println("I'm processor " + rank);
            Element[][] A = new Element[k][ord];
            for (int i = 0; i < k; i++) {
                A[i] = (Element[]) MPITransport.recvObject(0, 100 + rank);
                System.out.println("Rank = " + rank + "; Row = " + Array.toString(A[i]) + ";");
            }
            Element[] B = (Element[])
            MPITransport.recvObject(0, 100 + rank);
            System.out.println("Rank = " + rank + "; B = " + Array.toString(B) + ";");
            Element[] result = new Element[k];
            for (int j = 0; j < A.length; j++)
                result[j] = new VectorS(A[j]).multiply(new VectorS(B), ring);
            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("Sending result");
        }
        MPI.Finalize();
    }
}

/*
Command:
mpirun -np 2 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task3/MatrixMulVector 2

Output:
I'm processor 1
Matrix A = 
[[0.6,  0.36]
 [0.43, 0.47]]
Vector B = [0.14, 0.23]
Rank = 0; Row = [0.60, 0.36];
Rank = 1; Row = [0.43, 0.47];
Rank = 1; B = [0.14, 0.23];
Sending result
A * B = [null, null]

*/