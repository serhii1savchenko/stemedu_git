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

/*

mpirun -n 8 java -cp target/classes/ "com.mathpar.students.ukma17m1.yanivskyy.src.matrix.MatrixMul8"

I'm processor 1
I'm processor 7
I'm processor 6
I'm processor 2
I'm processor 5
I'm processor 4
I'm processor 3
A = 
[[0.68, 0.15, 0.85, 0.51]
 [0.63, 0.36, 0.01, 0.3 ]
 [0.8,  0.89, 0.68, 0.01]
 [0.61, 0.64, 0.91, 0.65]]
B = 
[[0.2,  0.65, 0.79, 0.23]
 [0.84, 0.62, 0.99, 0.91]
 [0.57, 0.16, 0.76, 0.79]
 [0.14, 0.9,  0.38, 0.73]]
RES = 
[[0.82, 1.13, 1.53, 1.33]
 [0.48, 0.9,  0.98, 0.69]
 [1.3,  1.19, 2.03, 1.54]
 [1.27, 1.52, 2.05, 1.9 ]]

*/
import com.mathpar.parallel.utils.MPITransport;
import com.mathpar.number.Ring;
import com.mathpar.number.NumberZp32;
import mpi.MPIException;
import mpi.MPI;
import com.mathpar.matrix.MatrixS;
import java.util.Random;
import java.io.IOException;

public class MatrixMul8 {
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        Ring ring = new Ring("R64[x]");

        MPI.Init(new String[0]);

        int rank = MPI.COMM_WORLD.getRank();
        if (rank == 0) {
            int ord = 4;
            int den = 10000;

            Random rnd = new Random();

            MatrixS A = new MatrixS(ord, ord, den, new int[] {5, 3}, rnd, NumberZp32.ONE, ring);
            System.out.println("A = " + A);
            MatrixS B = new MatrixS(ord, ord, den, new int[] {5, 3}, rnd, NumberZp32.ONE, ring);
            System.out.println("B = " + B);
            MatrixS D = null;
            MatrixS[] AA = A.split();

            MatrixS[] BB = B.split();
            int tag = 0;

            MPITransport.sendObjectArray(new Object[] {AA[1], BB[2]}, 0, 2, 1, tag);
            MPITransport.sendObjectArray(new Object[] {AA[0], BB[1]}, 0, 2, 2, tag);
            MPITransport.sendObjectArray(new Object[] {AA[1], BB[3]}, 0, 2, 3, tag);
            MPITransport.sendObjectArray(new Object[] {AA[2], BB[0]}, 0, 2, 4, tag);
            MPITransport.sendObjectArray(new Object[] {AA[3], BB[2]}, 0, 2, 5, tag);
            MPITransport.sendObjectArray(new Object[] {AA[2], BB[1]}, 0, 2, 6, tag);
            MPITransport.sendObjectArray(new Object[] {AA[3], BB[3]}, 0, 2, 7, tag);
            MatrixS[] DD = new MatrixS[4];

            DD[0] = (AA[0].multiply(BB[0], ring)).
                    add((MatrixS) MPITransport.recvObject(1, 3), ring);
            DD[1] = (MatrixS) MPITransport.recvObject(2, 3);
            DD[2] = (MatrixS) MPITransport.recvObject(4, 3);
            DD[3] = (MatrixS) MPITransport.recvObject(6, 3);
            D = MatrixS.join(DD);
            System.out.println("RES = " + D.toString());
        } else {
            System.out.println("I'm processor " + rank);
            Object[] b = new Object[2];
            MPITransport.recvObjectArray(b, 0, 2, 0, 0);
            MatrixS[] a = new MatrixS[b.length];
            for (int i = 0; i < b.length; i++) {
                a[i] = (MatrixS) b[i];
            }
            MatrixS res = a[0].multiply(a[1], ring);
            if (rank % 2 == 0) {
                MatrixS p = res.add((MatrixS) MPITransport.
                        recvObject(rank + 1, 3), ring);
                MPITransport.sendObject(p, 0, 3);
            } else {
                MPITransport.sendObject(res, rank - 1, 3);
            }
        }
        MPI.Finalize();
    }
}
