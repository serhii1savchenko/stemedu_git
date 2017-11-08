package com.mathpar.students.ukma17i41.sukharskyi;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Random;

public class Matrixes {
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        // multiply on 4 processors
        // run command:
        //   openmpi/bin/mpirun --hostfile hostfile  -np 4 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/Matrixes
        // MatrixMul4.multiplyMatrixes(args);

        // multiply on 8 processors
        // run command:
        //   openmpi/bin/mpirun --hostfile hostfile  -np 8 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/Matrixes
        MatrixMul8.multiplyMatrixes(args);
    }
}

class MatrixMul4 {
    static int tag = 0;
    static int mod = 13;

    private static MatrixS mmultiply(MatrixS a, MatrixS b, MatrixS c, MatrixS d, Ring ring) {
        return (a.multiply(b, ring)).add(c.multiply(d, ring), ring);
    }

    public static void multiplyMatrixes(String[] args)
            throws MPIException, IOException,
            ClassNotFoundException {
        Ring ring = new Ring("R64[x]");
        MPI.Init(new String[0]);
        int rank = MPI.COMM_WORLD.getRank();

        if (rank == 0) {
            ring.setMOD32(mod);
            int ord = 4;
            int den = 10000;
            Random rnd = new Random();

            MatrixS A = new MatrixS(ord, ord, den,
                    new int[]{5, 5}, rnd, NumberZp32.ONE, ring);
            MatrixS B = new MatrixS(ord, ord, den,
                    new int[]{5, 5}, rnd, NumberZp32.ONE, ring);

            MatrixS[] DD = new MatrixS[4];
            MatrixS CC = null;
            MatrixS[] AA = A.split();
            MatrixS[] BB = B.split();

            MPITransport.sendObjectArray(new Object[]{
                    AA[0], BB[1], AA[1], BB[3]}, 0, 4, 1, 1);
            MPITransport.sendObjectArray(new Object[]{
                    AA[2], BB[0], AA[3], BB[2]}, 0, 4, 2, 2);
            MPITransport.sendObjectArray(new Object[]{
                    AA[2], BB[1], AA[3], BB[3]}, 0, 4, 3, 3);

            DD[0] = (AA[0].multiply(BB[0], ring)).
                    add(AA[1].multiply(BB[2], ring), ring);
            DD[1] = (MatrixS) MPITransport.recvObject(1, 1);
            System.out.println("recv 1 to 0");
            DD[2] = (MatrixS) MPITransport.recvObject(2, 2);
            System.out.println("recv 2 to 0");
            DD[3] = (MatrixS) MPITransport.recvObject(3, 3);
            System.out.println("recv 3 to 0");
            CC = MatrixS.join(DD);

            System.out.println("RES= " + CC.toString());
        } else {
            System.out.println("I'm processor " + rank);
            ring.setMOD32(mod);

            Object[] n = new Object[4];

            MPITransport.recvObjectArray(n, 0, 4, 0, rank);
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

class MatrixMul8 {
    static void multiplyMatrixes(String[] args)
            throws MPIException, IOException, ClassNotFoundException {
        Ring ring = new Ring("R64[x]");
        MPI.Init(new String[0]);
        int rank = MPI.COMM_WORLD.getRank();

        if (rank == 0) {
            int ord = 4;
            int den = 10000;
            Random rnd = new Random();

            MatrixS A = new MatrixS(ord, ord, den,
                    new int[]{5, 3}, rnd, NumberZp32.ONE, ring);
            System.out.println("A = " + A);
            MatrixS B = new MatrixS(ord, ord, den,
                    new int[]{5, 3}, rnd, NumberZp32.ONE, ring);
            System.out.println("B = " + B);

            MatrixS D = null;
            MatrixS[] AA = A.split();
            MatrixS[] BB = B.split();

            int tag = 0;

            MPITransport.sendObjectArray
                    (new Object[]{AA[1], BB[2]}, 0, 2, 1, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[0], BB[1]}, 0, 2, 2, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[1], BB[3]}, 0, 2, 3, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[2], BB[0]}, 0, 2, 4, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[3], BB[2]}, 0, 2, 5, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[2], BB[1]}, 0, 2, 6, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[3], BB[3]}, 0, 2, 7, tag);
            MatrixS[] DD = new MatrixS[4];

            DD[0] = (AA[0].multiply(BB[0], ring)).
                    add((MatrixS) MPITransport.recvObject(1, 3),
                            ring);
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

            for (int i = 0; i < b.length; i++)
                a[i] = (MatrixS) b[i];

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
