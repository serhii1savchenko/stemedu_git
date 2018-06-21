import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Random;


public class MatrixMult4 {

	static /**
     * docker@estimate:/tmp/module-2/out/production/module-2$ mpirun -np 4 java MatrixMult4
     * processor 2
     * processor 1
     * processor 3
     * res =
     * [[0.77, 0.65]
     * [1.03, 0.86]]
     * res =
     * [[0.49, 0.97]
     * [0.71, 1.38]]
     * res =
     * [[0.9,  1.06]
     * [0.92, 0.56]]
     * send result
     * send result
     * send result
     * recv 1 to 0
     * recv 2 to 0
     * recv 3 to 0
     * RES=
     * [[0.85, 1.62, 0.9,  1.06]
     * [0.51, 1.27, 0.92, 0.56]
     * [0.49, 0.97, 0.77, 0.65]
     * [0.71, 1.38, 1.03, 0.86]]
     */sult
	 *  recv 1 to 0
	 *  recv 2 to 0
	 *  recv 3 to 0
	 *  RES=
	 *  [[0.85, 1.62, 0.9,  1.06]
	 *   [0.51, 1.27, 0.92, 0.56]
	 *   [0.49, 0.97, 0.77, 0.65]
	 *   [0.71, 1.38, 1.03, 0.86]]
	 */
	public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
		Ring ring = new Ring("R64[x]");
		MPI.Init(new String[0]);

		int rank = MPI.COMM_WORLD.getRank();

		if (rank == 0) {
			ring.setMOD32(mod);

			int ord = 4;
			int den = 10000;
			Random rnd = new Random();
			MatrixS A = new MatrixS(ord, ord, den,
					new int[] {5, 5}, rnd, NumberZp32.ONE, ring);
			MatrixS B = new MatrixS(ord, ord, den,
					new int[] {5, 5}, rnd, NumberZp32.ONE, ring);
			MatrixS[] DD = new MatrixS[4];
			MatrixS CC = null;
			MatrixS[] AA = A.split();
			MatrixS[] BB = B.split();

			MPITransport.sendObjectArray(new Object[] { AA[0], BB[1], AA[1], BB[3] }, 0, 4, 1, 1);
			MPITransport.sendObjectArray(new Object[] { AA[2], BB[0], AA[3], BB[2] }, 0, 4, 2, 2);
			MPITransport.sendObjectArray(new Object[] { AA[2], BB[1], AA[3], BB[3] }, 0, 4, 3, 3);

			MatrixS[] DD = new MatrixS[4];
			DD[0] = AA[0].multiply(BB[0], ring).add(AA[1].multiply(BB[2], ring), ring);
			for(int i = 1; i <= 3; i++) {
				DD[i] = (MatrixS) MPITransport.recvObject(i, i);
				System.out.println("recv " + i + " to 0");
			}

			CC = MatrixS.join(DD);
			System.out.println("RES = " + CC.toString());
		} else {
			System.out.println("processor " + rank);
			ring.setMOD32(mod);

			Object[] n = new Object[4];
			MPITransport.recvObjectArray(n,0,4, 0, rank);
			MatrixS a = (MatrixS) n[0];
			MatrixS b = (MatrixS) n[1];
			MatrixS c = (MatrixS) n[2];
			MatrixS d = (MatrixS) n[3];

			MatrixS res = mmultiply(a, b, c, d, ring);

			System.out.println("res = " + res);
			MPITransport.sendObject(res, 0, rank);
			System.out.println("send result");
		}

		MPI.Finalize();
	}

}
