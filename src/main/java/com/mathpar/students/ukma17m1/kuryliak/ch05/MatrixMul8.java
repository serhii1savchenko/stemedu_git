package com.mathpar.students.ukma17m1.kuryliak.ch05;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Random;

public class MatrixMul8 {
    static int tag = 0;
    static int mod = 13;

    // mpirun -np 8 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch05.MatrixMul8
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        Ring ring = new Ring("R64[x]");
        MPI.Init(new String[0]);
        int rank = MPI.COMM_WORLD.getRank();

        if(rank == 0) {
            ring.setMOD32(mod);
            int ord = 4;
            int den = 10000;
            Random rnd = new Random();

            MatrixS A = new MatrixS(ord, ord, den, new int[] {5, 3}, rnd, NumberZp32.ONE, ring);
            System.out.println("A = " + A);
            MatrixS B = new MatrixS(ord, ord, den, new int[] {5, 3}, rnd, NumberZp32.ONE, ring);
            System.out.println("B = " + B);

            MatrixS[] AA = A.split();
            MatrixS[] BB = B.split();

            MPITransport.sendObjectArray(new Object[]{AA[1], BB[2]}, 0, 2, 1, tag);
            MPITransport.sendObjectArray(new Object[]{AA[0], BB[1]}, 0, 2, 2, tag);
            MPITransport.sendObjectArray(new Object[]{AA[1], BB[3]}, 0, 2, 3, tag);
            MPITransport.sendObjectArray(new Object[]{AA[2], BB[0]}, 0, 2, 4, tag);
            MPITransport.sendObjectArray(new Object[]{AA[3], BB[2]}, 0, 2, 5, tag);
            MPITransport.sendObjectArray(new Object[]{AA[2], BB[1]}, 0, 2, 6, tag);
            MPITransport.sendObjectArray(new Object[]{AA[3], BB[3]}, 0, 2, 7, tag);

            MatrixS[] DD = new MatrixS[4];
            DD[0] = AA[0].multiply(BB[0], ring).add(AA[1].multiply((MatrixS) MPITransport.recvObject(1, 3), ring), ring);
            DD[1] = (MatrixS) MPITransport.recvObject(2, 3);
            DD[2] = (MatrixS) MPITransport.recvObject(4, 3);
            DD[3] = (MatrixS) MPITransport.recvObject(6, 3);

            MatrixS D = MatrixS.join(DD);
            System.out.println("RES = " + D);
        } else {
            System.out.println("I'm processor " + rank);
            ring.setMOD32(mod);

            MatrixS[] b = new MatrixS[2];
            MPITransport.recvObjectArray(b, 0, 2, 0, 0);
            MatrixS[] a = new MatrixS[b.length];
            for(int i = 0; i < b.length; i++) {
                a[i] = b[i];
            }

            MatrixS res = a[0].multiply(a[1], ring);
            if(rank % 2 == 0) {
                MatrixS p = res.add((MatrixS) MPITransport.recvObject(rank+1, 3), ring);
                MPITransport.sendObject(p, 0, 3);
            } else {
                MPITransport.sendObject(res, rank-1, 3);
            }
        }
        MPI.Finalize();
    }

    /*
    I'm processor 3
    I'm processor 6
    I'm processor 4
    I'm processor 7
    I'm processor 2
    I'm processor 5
    I'm processor 1
    A =
    [[0.56, 0.62, 0.78, 0.28]
     [0.86, 0.77, 0.74, 0.22]
     [0.3,  0.14, 0.82, 0.44]
     [0.42, 0.6,  0.62, 0.69]]
    B =
    [[0.72, 0.51, 1,    0.87]
     [0.38, 0.44, 0.94, 0.85]
     [0.92, 0.66, 0.8,  0.58]
     [0.5,  0.04, 0.63, 0.13]]
    RES =
    [[1.52, 1.11, 1.94, 1.51]
     [1.71, 1.28, 2.3,  1.86]
     [1.25, 0.78, 1.36, 0.92]
     [1.44, 0.92, 1.9,  1.32]]
    */
}
