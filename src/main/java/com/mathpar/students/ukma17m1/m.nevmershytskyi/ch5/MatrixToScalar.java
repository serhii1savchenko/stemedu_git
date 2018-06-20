package com.mathpar.students.ukma17m1.m.nevmershytskyi.ch5;

import com.mathpar.number.*;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Random;

/*
 * INPUT: mpirun -np 4 java -cp D:\java\stemedu\target\classes com.mathpar.students.ukma17m1.m.nevmershytskyi.ch5.MatrixToScalar 4 8
 */

/*
 * OUTPUT: 
	Proc: 1
	Proc: 3
	Proc: 2
	Vector B = [6, 14, 18, 28]
	rank = 3 B = [28]
	rank = 2 B = [18]
	sendObject<result>
	sendObject<result>
	rank = 1 B = [14]
	sendObject<result>
	B * S = [48, 112, 144, 224]
 */

public class MatrixToScalar {

	public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {

		Ring ring = new Ring("Z[x]");
		MPI.Init(args);
		
		int rank = MPI.COMM_WORLD.getRank();
		int size = MPI.COMM_WORLD.getSize();

		int ord = Integer.parseInt(args[0]);

		Element s = NumberR64.valueOf(Integer.parseInt(args[1]));

		int k = ord / size;

		int n = ord - k * (size - 1);

		if (rank == 0) {
			final int density = 10000;

			VectorS B = new VectorS(ord, density, new int[] {5, 5}, new Random(), ring);
			System.out.println("vector<B> = " + B);
			Element[] res0 = new Element[n];

			for (int i = 0; i < n; i++) {
				res0[i] = B.V[i].multiply(s, ring);
			}

			for (int j = 1; j < size; j++) {
				Element[] v = new Element[k];
				System.arraycopy(B.V, n + (j - 1) * k, v, 0, k);
				MPITransport.sendObject(v, j, 100 + j);
			}

			Element[] result = new Element[ord];
			System.arraycopy(res0, 0, result, 0, n);

			for (int t = 1; t < size; t++) {
				Element[] resRank = (Element[]) MPITransport.recvObject(t, 100 + t);
				System.arraycopy(resRank, 0, result, n + (t - 1) * k, resRank.length);
			}

			System.out.println("B * S = " + new VectorS(result).toString(ring));
		} else {
			System.out.println("Proc: " + rank);
			
			Element[] B = (Element[]) MPITransport.recvObject(0, 100 + rank);
			System.out.println("rank = " + rank + " B = " + Array.toString(B));
			
			Element[] result = new Element[k];

			for (int j = 0; j < B.length; j++) {
				result[j] = B[j].multiply(s, ring);
			}

			MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("sendObject<result>");
		}

		MPI.Finalize();
	}
	
}
