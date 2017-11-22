package com.mathpar.students.ukma17i41.sukharskyi;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.*;
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
        // Output
        //        I'm processor 2
        //        I'm processor 3
        //        I'm processor 1
        //        res =
        //        [[1.09, 0.54]
        //         [1.47, 0.82]]
        //                send result
        //                res =
        //        [[1.12, 0.57]
        //         [1.44, 1.08]]
        //                send result
        //                res =
        //        [[0.41, 0.11]
        //         [1.34, 0.51]]
        //                send result
        //                recv 1 to 0
        //                recv 2 to 0
        //                recv 3 to 0
        //                RES=
        //        [[0.4,  0.23, 0.41, 0.11]
        //         [1.41, 0.62, 1.34, 0.51]
        //         [1.12, 0.57, 1.09, 0.54]
        //         [1.44, 1.08, 1.47, 0.82]]
        //MatrixMul4.multiplyMatrixes(args);

        // multiply on 8 processors
        // run command:
        //   openmpi/bin/mpirun --hostfile hostfile  -np 8 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/Matrixes
        //        I'm processor 4
        //        I'm processor 1
        //        I'm processor 5
        //        I'm processor 2
        //        I'm processor 7
        //        I'm processor 6
        //        I'm processor 3
        //        A =
        //        [[0.06, 0.39, 0.51, 0.1 ]
        //         [0.8,  0.38, 0.76, 0.09]
        //         [0.95, 0.26, 0.24, 0.5 ]
        //         [0,    0.75, 0.13, 0.96]]
        //                B =
        //        [[0.66, 0.17, 0.19, 0.62]
        //         [0.72, 0.43, 0.88, 0.83]
        //         [0.39, 0.65, 0.62, 0.3 ]
        //         [0.14, 0.55, 0.48, 0.68]]
        //                RES =
        //        [[0.53, 0.56, 0.71, 0.58]
        //         [1.11, 0.84, 1,    1.1 ]
        //         [0.98, 0.71, 0.8,  1.22]
        //         [0.73, 0.93, 1.2,  1.33]]
        //MatrixMul8.multiplyMatrixes(args);

        // run
        //     openmpi/bin/mpirun --hostfile hostfile  -np 8 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/Matrixes 2
        // output
        //        I'm processor 4
        //        I'm processor 1
        //        Matrix A =
        //        [[28, 27]
        //        [25, 24]]
        //        I'm processor 2
        //        I'm processor 5
        //        I'm processor 6
        //        Vector B = [26, 23]
        //        rank = 0 row = [28, 27]
        //        rank = 0 row = [25, 24]
        //        I'm processor 3
        //        I'm processor 7
        //        rank = 1 B = [26, 23]
        //        send result
        //        rank = 7 B = [26, 23]
        //        send result
        //        rank = 4 B = [26, 23]
        //        rank = 5 B = [26, 23]
        //        send result
        //        send result
        //        rank = 6 B = [26, 23]
        //        rank = 3 B = [26, 23]
        //        send result
        //        send result
        //        rank = 2 B = [26, 23]
        //        send result
        //        A * B = [[[728, 644],
        //        [702, 621]], [[650, 575],
        //        [624, 552]]]
        //MultiplyMatrixToVector.multiplyMatrToVect(args);

        // run command
        //  openmpi/bin/mpirun --hostfile hostfile  -np 8 java -cp /home/probook/Documents/education/parralel-programming/stemedu/stemedu/target/classes  com/mathpar/students/ukma17i41/sukharskyi/Matrixes 2 2
        // output
        //        I'm processor 3
        //        I'm processor 4
        //        I'm processor 1
        //        I'm processor 5
        //        I'm processor 6
        //        I'm processor 7
        //        I'm processor 2
        //        Vector B = [28, 19]
        //        rank = 3 B = []
        //        send result
        //        rank = 7 B = []
        //        send result
        //        rank = 1 B = []
        //        rank = 5 B = []
        //        send result
        //        rank = 4 B = []
        //        send result
        //        send result
        //        rank = 6 B = []
        //        send result
        //        rank = 2 B = []
        //        send result
        //        B * S = [56, 38]
        MultiplyVectorToScalar.multVectToSclr(args);
    }
}

class MultiplyMatrixToVector
{
    public static void multiplyMatrToVect(String[] args)throws MPIException, IOException,ClassNotFoundException
    {
        Ring ring = new Ring("Z[x]");
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        int ord = Integer.parseInt(args[0]);
        int k = ord / size;
        int n = ord - k * (size - 1);

        if (rank == 0)
        {
            int den = 10000;
            Random rnd = new Random();
            MatrixS A = new MatrixS(ord, ord, den, new int[] {5, 5}, rnd, NumberZp32.ONE, ring);
            System.out.println("Matrix A = " + A);
            VectorS B = new VectorS(ord, den, new int[] {5, 5}, rnd, ring);
            System.out.println("Vector B = " + B);
            Element[] res0 = new Element[n];
            for (int i = 0; i < n; i++)
            {
                res0[i] = new VectorS(A.M[i]).transpose(ring).multiply(B, ring);
                System.out.println("rank = " + rank + " row = " + Array.toString(A.M[i]));
            }

            for (int j = 1; j < size; j++)
            {
                for (int z = 0; z < k; z++)
                    MPITransport.sendObject(A.M[n + (j - 1) * k+ j * z], j, 100 + j);
                MPITransport.sendObject(B.V, j, 100 + j);
            }

            Element[] result = new Element[ord];
            System.arraycopy(res0, 0, result, 0, n);
            for (int t = 1; t < size; t++)
            {
                Element[] resRank = (Element[])MPITransport.recvObject(t, 100 + t);
                System.arraycopy(resRank, 0, result, n +(t - 1) * k, resRank.length);
            }
            System.out.println("A * B = " + new VectorS(result).toString(ring));
        }
        else
        {
            System.out.println("I'm processor " + rank);
            Element[][] A = new Element[k][ord];
            for (int i = 0; i < k; i++)
            {
                A[i] = (Element[])MPITransport.recvObject(0, 100 + rank);
                System.out.println("rank = " + rank + "row = " + Array.toString(A[i]));
            }
            Element[] B = (Element[])MPITransport.recvObject(0, 100 + rank);
            System.out.println("rank = " + rank + " B = "+ Array.toString(B));
            Element[] result = new Element[k];
            for (int j = 0; j < A.length; j++)
                result[j] = new VectorS(A[j]).transpose(ring).multiply(new VectorS(B), ring);
            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("send result");
        }
        MPI.Finalize();
    }
}

class MultiplyVectorToScalar
{
    public static void multVectToSclr(String[] args)throws MPIException, IOException,ClassNotFoundException
    {
        Ring ring = new Ring("Z[x]");
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();
        int ord = Integer.parseInt(args[0]);
        Element s = NumberR64.valueOf(Integer.parseInt(args[1]));

        int k = ord / size;
        int n = ord - k * (size - 1);
        if (rank == 0)
        {
            int den = 10000;
            Random rnd = new Random();
            VectorS B = new VectorS(ord, den, new int[] {5, 5}, rnd, ring);
            System.out.println("Vector B = " + B);
            Element[] res0 = new Element[n];

            for (int i = 0; i < n; i++)
                res0[i] = B.V[i].multiply(s, ring);

            for (int j = 1; j < size; j++) {
                Element[] v = new Element[k];
                System.arraycopy(B.V, n + (j - 1) * k, v, 0, k);
                MPITransport.sendObject(v, j, 100 + j);
            }

            Element[] result = new Element[ord];
            System.arraycopy(res0, 0, result, 0, n);

            for (int t = 1; t < size; t++)
            {
                Element[] resRank = (Element[])MPITransport.recvObject(t, 100 + t);
                System.arraycopy(resRank, 0, result, n +(t - 1) * k, resRank.length);
            }
            System.out.println("B * S = " + new VectorS(result).toString(ring));
        }
        else
        {
            System.out.println("I'm processor " + rank);
            Element[] B = (Element[])MPITransport.recvObject(0, 100 + rank);
            System.out.println("rank = " + rank +" B = " + Array.toString(B));
            Element[] result = new Element[k];

            for (int j = 0; j < B.length; j++)
                result[j] = B[j].multiply(s, ring);
            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("send result");
        }
        MPI.Finalize();
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

            DD[0] = (AA[0].multiply(BB[0], ring)).add((MatrixS) MPITransport.recvObject(1, 3),ring);
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
