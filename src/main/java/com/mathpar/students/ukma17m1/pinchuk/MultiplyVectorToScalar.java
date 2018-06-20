package com.mathpar.students.ukma17m1.pinchuk;

import java.io.IOException;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;
import com.mathpar.number.Array;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.parallel.utils.MPITransport;

//mpirun -np 4 java -cp /home/lida/Desktop/project/stemedu/target/classes/ com/mathpar/students/ukma17m1/pinchuk/MultiplyVectorToScalar 8 16

/*

I'm processor 3
I'm processor 2
I'm processor 1
Vector B = [9, 9, 18, 3, 16, 5, 22, 21]
rank = 1 B = [18, 3]
rank = 3 B = [22, 21]
send result
send result
rank = 2 B = [16, 5]
send result
B * S = [144, 144, 288, 48, 256, 80, 352, 336]

*/

public class MultiplyVectorToScalar {
	
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
			int den = 10000;
			Random rnd = new Random();
			VectorS B = new VectorS(ord, den, new int[] {5, 5}, rnd, ring);
			System.out.println("Vector B = " + B);
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
			System.out.println("I'm processor " + rank);
			Element[] B = (Element[]) MPITransport.recvObject(0, 100 + rank);
			System.out.println("rank = " + rank + " B = " + Array.toString(B));
			Element[] result = new Element[k];
			for (int j = 0; j < B.length; j++) {
				result[j] = B[j].multiply(s, ring);
			}
			MPITransport.sendObject(result, 0, 100 + rank);
			System.out.println("send result");
		}
		MPI.Finalize();
	}
}