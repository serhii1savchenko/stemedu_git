import java.io.IOException;
import java.util.Random;

import com.mathpar.number.Array;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.parallel.utils.MPITransport;

import mpi.MPI;
import mpi.MPIException;


public class MatrixToScalar {

    /**
     * docker@estimate:/tmp/module-2/out/production/module-2$ mpirun -np 4 java MatrixToScalar 4 8
     * processor 1
     * processor 3
     * processor 2
     * Vector B = [6, 14, 18, 28]
     * rank = 3 B = [28]
     * rank = 2 B = [18]
     * send result
     * send result
     * rank = 1 B = [14]
     * send result
     * B x S = [48, 112, 144, 224]
     */
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
            VectorS B = new VectorS(ord, den, new int[]{5, 5}, rnd, ring);
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
            System.out.println("B x S = " + new VectorS(result).toString(ring));
        } else {
            System.out.println("processor " + rank);
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
