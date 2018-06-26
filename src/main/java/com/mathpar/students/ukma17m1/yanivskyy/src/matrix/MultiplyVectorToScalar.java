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

/*

mpirun -n 8 java -cp target/classes/ "com.mathpar.students.ukma17m1.yanivskyy.src.matrix.MultiplyVectorToScalar" 25 25

I'm processor 2
I'm processor 7
I'm processor 6
I'm processor 3
I'm processor 1
I'm processor 4
I'm processor 5
Vector B = [18, 26, 11, 5, 8, 9, 8, 27, 18, 15, 12, 3, 11, 31, 25, 3, 10, 14, 18, 4, 28, 2, 21, 28, 2]
rank = 2 B = [27, 18, 15]
rank = 1 B = [8, 9, 8]
send result
rank = 5 B = [10, 14, 18]
send result
send result
rank = 4 B = [31, 25, 3]
rank = 7 B = [21, 28, 2]
rank = 3 B = [12, 3, 11]
rank = 6 B = [4, 28, 2]
send result
send result
send result
send result
B * S = [450, 650, 275, 125, 200, 225, 200, 675, 450, 375, 300, 75, 275, 775, 625, 75, 250, 350, 450, 100, 700, 50, 525, 700, 50]

*/
public class MultiplyVectorToScalar {
    public static void main(String[] args)
            throws MPIException, IOException,
            ClassNotFoundException {
        Ring ring = new Ring("Z[x]");
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        int ord = Integer.parseInt(args[0]);
        Element s = NumberR64.valueOf(
                Integer.parseInt(args[1]));

        int k = ord / size;

        int n = ord - k * (size - 1);
        if (rank == 0) {
            int den = 10000;
            Random rnd = new Random();
            VectorS B = new VectorS(ord, den,
                    new int[] {5, 5}, rnd, ring);
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
            System.out.println("B * S = "
                    + new VectorS(result).toString(ring));
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
