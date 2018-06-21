package com.mathpar.students.ukma17m1.m.nevmershytskyi.ch5;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.*;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Random;

/*
 * INPUT: mpirun -np 4 java -cp D:\java\stemedu\target\classes com.mathpar.students.ukma17m1.m.nevmershytskyi.ch5.MatrixToVector 4
 */

/*
 * OUTPUT: 
	Proc: 2
	Proc: 3
	Proc: 1
	Matrix A = 
		[[4,  28, 3,  13]
 		[13, 0,  25, 3 ]
 		[23, 9,  28, 15]
 		[7,  3,  2,  2 ]]
	Vector B = [4, 16, 7, 6]
	rank = 0 row = [4, 28, 3, 13]
	rank = 3 row = [7, 3, 2, 2]
	rank = 3 B = [4, 16, 7, 6]
	sendObject<result>
	rank = 2 row = [23, 9, 28, 15]rank = 1 row = [13, 25, 3]
	rank = 1 B = [4, 16, 7, 6]

	rank = 2 B = [4, 16, 7, 6]
	sendObject<result>
	sendObject<result>
	A * B = [null, [[52, 208, 91, 78],
			[100, 400, 175, 150],
			[12, 48, 21, 18]], [[92, 368, 161, 138],
			[36, 144, 63, 54],
			[112, 448, 196, 168],
			[60, 240, 105, 90]], [[28, 112, 49, 42],
			[12, 48, 21, 18],
			[8, 32, 14, 12],
			[8, 32, 14, 12]]]

 */

public class MatrixToVector {

	public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {

		Ring ring = new Ring("Z[x]");
		MPI.Init(args);

		int rank = MPI.COMM_WORLD.getRank();
		int size = MPI.COMM_WORLD.getSize();
		int ord = Integer.parseInt(args[0]);
		int k = ord / size;
		int n = ord - k * (size - 1);

		if (rank == 0) {
			final int density = 10000;
			MatrixS A = new MatrixS(ord, ord, density, new int[] {5, 5}, new Random(), NumberZp32.ONE, ring);
			System.out.println("Matrix<A> = " + A);
			VectorS B = new VectorS(ord, density, new int[] {5, 5}, new Random(), ring);
			System.out.println("Vector<B> = " + B);
			Element[] res0 = new Element[n];

			for (int i = 0; i < n; i++) {
				res0[i] = new VectorS(A.M[i]).multiply(B, ring);
				System.out.println("rank = " + rank + " row = " + Array.toString(A.M[i]));
			}
			
			for (int j = 1; j < size; j++) {
				for (int z = 0; z < k; z++) {
					MPITransport.sendObject(A.M[n + (j - 1) * k + j * z], j, 100 + j);
				}
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
			System.out.println("Proc: " + rank);
			Element[][] A = new Element[k][ord];

			for (int i = 0; i < k; i++) {
				A[i] = (Element[]) MPITransport.recvObject(0, 100 + rank);
				System.out.println("rank = " + rank + " row = " + Array.toString(A[i]));
			}

			Element[] B = (Element[]) MPITransport.recvObject(0, 100 + rank);
			System.out.println("rank = " + rank + " B = " + Array.toString(B));
			Element[] result = new Element[k];

			for (int j = 0; j < A.length; j++) {
				result[j] = new VectorS(A[j]).transpose(ring).multiply(new VectorS(B), ring);
			}

			MPITransport.sendObject(result, 0, 100 + rank);
			System.out.println("sendObject<result>");
		}

		MPI.Finalize();
	}
}
