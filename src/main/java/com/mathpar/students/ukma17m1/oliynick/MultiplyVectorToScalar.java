package com.mathpar.students.ukma17m1.oliynick;

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

Processor 3
Processor 2
Processor 1

Vector B = [9, 9, 18, 3, 16, 5, 22, 21]

rank = 1 B = [18, 3]

rank = 3 B = [22, 21]

send result
send result

rank = 2 B = [16, 5]

send result
B * S = [144, 144, 288, 48, 256, 80, 352, 336]

*/

public final class MultiplyVectorToScalar {
	
	public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {

		final Ring ring = new Ring("Z[x]");

		MPI.Init(args);

		final int rank = MPI.COMM_WORLD.getRank();
		final int size = MPI.COMM_WORLD.getSize();
		
		final int ord = Integer.parseInt(args[0]);
		
		final Element s = NumberR64.valueOf(Integer.parseInt(args[1]));

		final int k = ord / size;
		final int n = ord - k * (size - 1);
		
		if (rank == 0) {
			final int den = 10000;
			final Random rnd = new Random();
			final VectorS B = new VectorS(ord, den, new int[] {5, 5}, rnd, ring);

			System.out.println(String.format("Vector B = %s", B));

			final Element[] res0 = new Element[n];

			for (int i = 0; i < n; i++) {
				res0[i] = B.V[i].multiply(s, ring);
			}

			for (int j = 1; j < size; j++) {
				final Element[] v = new Element[k];

				System.arraycopy(B.V, n + (j - 1) * k, v, 0, k);
				MPITransport.sendObject(v, j, 100 + j);
			}

			final Element[] result = new Element[ord];

			System.arraycopy(res0, 0, result, 0, n);

			for (int t = 1; t < size; t++) {
				final Element[] resRank = (Element[]) MPITransport.recvObject(t, 100 + t);

				System.arraycopy(resRank, 0, result, n + (t - 1) * k, resRank.length);
			}
			System.out.println(String.format("B * S = %s", new VectorS(result).toString(ring)));
		} else {
			System.out.println(String.format("Processor %d", rank));

			final Element[] B = (Element[]) MPITransport.recvObject(0, 100 + rank);

			System.out.println(String.format("rank = %d B = %s",rank , Array.toString(B)));

			final Element[] result = new Element[k];
			for (int j = 0; j < B.length; j++) {
				result[j] = B[j].multiply(s, ring);
			}
			MPITransport.sendObject(result, 0, 100 + rank);
			System.out.println("send result");
		}
		MPI.Finalize();
	}
}