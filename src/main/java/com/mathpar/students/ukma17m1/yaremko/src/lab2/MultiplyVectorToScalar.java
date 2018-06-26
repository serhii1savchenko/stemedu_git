/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yaremko.src.lab2;

/**
 *
 * @author Solomka
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

mpirun -n 8 java -cp target/classes/ "com.mathpar.students.ukma17m1.yaremko.src.lab2.MultiplyVectorToScalar" 20 20

I'm processor 2
I'm processor 6
I'm processor 5I'm processor 4I'm processor 3
I'm processor 1


I'm processor 7
Vector B = [1, 19, 22, 18, 12, 21, 7, 0, 4, 31, 19, 11, 31, 2, 25, 4, 3, 22, 14, 30]
rank = 2 B = [4, 31]
send result
rank = 7 B = [14, 30]
send result
rank = 1 B = [7, 0]
rank = 5 B = [25, 4]
rank = 4 B = [31, 2]
rank = 3 B = [19, 11]send result
rank = 6 B = [3, 22]

send result
send result
send result
send result
B * S = [20, 380, 440, 360, 240, 420, 140, 0, 80, 620, 380, 220, 620, 40, 500, 80, 60, 440, 280, 600]

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
