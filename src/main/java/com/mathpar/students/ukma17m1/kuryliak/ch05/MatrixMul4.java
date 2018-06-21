package com.mathpar.students.ukma17m1.kuryliak.ch05;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Random;

public class MatrixMul4 {
    static int tag = 0;
    static int mod = 13;

    public static MatrixS mmulitply(MatrixS a, MatrixS b, MatrixS c, MatrixS d, Ring ring) {
        return a.multiply(b, ring).add(c.multiply(d, ring), ring);
    }

    // mpirun -np 4 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch05.MatrixMul4
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        Ring ring = new Ring("R64[x]");
        MPI.Init(new String[0]);
        int rank = MPI.COMM_WORLD.getRank();

        if(rank == 0) {
            ring.setMOD32(mod);
            int ord = 4;
            int den = 10000;
            Random rnd = new Random();

            MatrixS A = new MatrixS(ord, ord, den, new int[] {5, 5}, rnd, NumberZp32.ONE, ring);
            MatrixS B = new MatrixS(ord, ord, den, new int[] {5, 5}, rnd, NumberZp32.ONE, ring);

            MatrixS[] AA = A.split();
            MatrixS[] BB = B.split();

            MPITransport.sendObjectArray(new Object[]{AA[0], BB[1], AA[1], BB[3]}, 0, 4, 1, 1);
            MPITransport.sendObjectArray(new Object[]{AA[2], BB[0], AA[3], BB[2]}, 0, 4, 2, 2);
            MPITransport.sendObjectArray(new Object[]{AA[2], BB[1], AA[3], BB[3]}, 0, 4, 3, 3);

            MatrixS[] DD = new MatrixS[4];
            DD[0] = AA[0].multiply(BB[0], ring).add(AA[1].multiply(BB[2], ring), ring);
            for(int i = 1; i <= 3; i++) {
                DD[i] = (MatrixS) MPITransport.recvObject(i, i);
                System.out.println("recv " + i + " to 0");
            }

            MatrixS CC = MatrixS.join(DD);
            System.out.println("RES= " + CC);
        } else {
            System.out.println("I'm processor " + rank);
            ring.setMOD32(mod);

            MatrixS[] n = new MatrixS[4];
            MPITransport.recvObjectArray(n, 0, 4, 0, rank);
            MatrixS a = n[0];
            MatrixS b = n[1];
            MatrixS c = n[2];
            MatrixS d = n[3];

            MatrixS res = mmulitply(a, b, c, d, ring);
            System.out.println("res = " + res);
            MPITransport.sendObject(res, 0, rank);
            System.out.println("send result");
        }
        MPI.Finalize();
    }

    /*
    I'm processor 1
    I'm processor 3
    I'm processor 2
    res =
    [[0.49, 0.97]
     [0.71, 1.38]]
    res =
    [[0.77, 0.65]
     [1.03, 0.86]]
    res =
    [[0.9,  1.06]
     [0.92, 0.56]]
    send result
    send result
    send result
    recv 1 to 0
    recv 2 to 0
    recv 3 to 0
    RES=
    [[0.85, 1.62, 0.9,  1.06]
     [0.51, 1.27, 0.92, 0.56]
     [0.49, 0.97, 0.77, 0.65]
     [0.71, 1.38, 1.03, 0.86]]
    */
}
