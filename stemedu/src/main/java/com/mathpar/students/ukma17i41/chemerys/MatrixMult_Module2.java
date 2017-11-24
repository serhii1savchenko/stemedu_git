/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.chemerys;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Array;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.parallel.utils.MPITransport;
import java.io.IOException;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author finstereule
 */
public class MatrixMult_Module2 {
public static void main(String[] args)
            throws MPIException, IOException, ClassNotFoundException {
        
        MatrixMul4.multiplyMatrixes(args);
        
        //MatrixMul8.multiplyMatrixes(args);   
        
        //MatrixMul16.multiplyMatrixes(args);
             
        //MultiplyMatrixToVector.multiplyMatrixToVector(args);
        
        //MultiplyVectorToScalar.multiplyVectorToScalar(args);


    }
}

/*
command:
halynachemerys/openmpi/bin/mpirun --hostfile hostfile  -np 4 java -cp /home/finstereule/halynachemerys/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/chemerys/MatrixMult_Module2

output:

Processor №3
Processor №1
Processor №2

result = 
[[1.15, 1.58]
 [0.78, 1.21]]
Sending result...


result = 
[[0.37, 0.7 ]
 [0.77, 1.25]]
Sending result...


result = 
[[1.1,  1.56]
 [0.99, 1.08]]
Sending result...

recv 1 to 0
recv 2 to 0
recv 3 to 0

RESULT = 
[[0.61, 0.81, 0.37, 0.7 ]
 [0.9,  1.48, 0.77, 1.25]
 [1.1,  1.56, 1.15, 1.58]
 [0.99, 1.08, 0.78, 1.21]]

*/

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
                    new int[] {5, 5}, rnd, NumberZp32.ONE, ring);
            MatrixS B = new MatrixS(ord, ord, den,
                    new int[] {5, 5}, rnd, NumberZp32.ONE, ring);

            MatrixS[] DD = new MatrixS[4];
            MatrixS CC = null;
            MatrixS[] AA = A.split();
            MatrixS[] BB = B.split();

            MPITransport.sendObjectArray(new Object[] {
                AA[0], BB[1], AA[1], BB[3]}, 0, 4, 1, 1);
            MPITransport.sendObjectArray(new Object[] {
                AA[2], BB[0], AA[3], BB[2]}, 0, 4, 2, 2);
            MPITransport.sendObjectArray(new Object[] {
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

            System.out.println("\nRESULT = " + CC.toString());
        } else {
            System.out.println("Processor №" + rank);
            ring.setMOD32(mod);

            Object[] n = new Object[4];

            MPITransport.recvObjectArray(n, 0, 4, 0, rank);
            MatrixS a = (MatrixS) n[0];
            MatrixS b = (MatrixS) n[1];
            MatrixS c = (MatrixS) n[2];
            MatrixS d = (MatrixS) n[3];
            MatrixS res = mmultiply(a, b, c, d, ring);

            System.out.println("\nresult = " + res);
            System.out.println("Sending result...\n");
            MPITransport.sendObject(res, 0, rank);
            
        }
        MPI.Finalize();
    }
}

/*
command: 
halynachemerys/openmpi/bin/mpirun --hostfile hostfile  -np 8 java -cp /home/finstereule/halynachemerys/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/chemerys/MatrixMult_Module2

output:

Processor №1
Processor №3
Processor №7
Processor №5
Processor №2
Processor №4
Processor №6

Matrix A = 
[[0.85, 0.94, 0.45, 0.87]
 [0.88, 0.37, 0.17, 0.43]
 [0.71, 0.04, 0.06, 0.57]
 [0.12, 0.69, 0.3,  0.31]]
Matrix B = 
[[0.13, 0.83, 0.88, 0.45]
 [0.48, 0.82, 0.13, 0.51]
 [0.19, 0.39, 0.12, 0.68]
 [0.17, 0.13, 0.48, 0.16]]
RESULT = 
[[0.8,  1.76, 1.33, 1.31]
 [0.4,  1.15, 1.04, 0.77]
 [0.22, 0.72, 0.91, 0.47]
 [0.46, 0.82, 0.37, 0.66]]

*/

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
            System.out.println("Matrix A = " + A);
            MatrixS B = new MatrixS(ord, ord, den,
                    new int[]{5, 3}, rnd, NumberZp32.ONE, ring);
            System.out.println("Matrix B = " + B);

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

            DD[0] = (AA[0].multiply(BB[0], ring)).add((MatrixS) 
                    MPITransport.recvObject(1, 3),ring);
            DD[1] = (MatrixS) MPITransport.recvObject(2, 3);
            DD[2] = (MatrixS) MPITransport.recvObject(4, 3);
            DD[3] = (MatrixS) MPITransport.recvObject(6, 3);

            D = MatrixS.join(DD);

            System.out.println("RESULT = " + D.toString());
        } else {
            System.out.println("Processor №" + rank);

            Object[] b = new Object[4];  

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


/*   
command:
halynachemerys/openmpi/bin/mpirun --hostfile hostfile -np 16 java -cp /home/finstereule/halynachemerys/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/chemerys/MatrixMult_Module2

output: 

Processor №4
Processor №5
Processor №2
Processor №6
Processor №1
Processor №10
Processor №13
Processor №7
Processor №11
Processor №8
Processor №15
Processor №14
Processor №12
Processor №9
Processor №3

Matrix A = 
[[0.77, 0.76, 0.28, 0.59]
 [0.69, 0.35, 0.21, 0.83]
 [0.86, 0.26, 0.78, 0.48]
 [0.61, 0.21, 0.05, 0.1 ]]
Matrix B = 
[[0.29, 0.93, 0.7,  0.01]
 [0.76, 0.66, 0.07, 0.03]
 [0.47, 0.54, 0.38, 0.62]
 [0.93, 0.17, 0.7,  0.69]]
RESULT = 
[[1.48, 1.47, 1.11, 0.62]
 [1.33, 1.13, 1.16, 0.73]
 [1.26, 1.48, 1.26, 0.84]
 [0.45, 0.75, 0.53, 0.11]]

*/
class MatrixMul16 {
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
            System.out.println("Matrix A = " + A);
            MatrixS B = new MatrixS(ord, ord, den,
                    new int[]{5, 3}, rnd, NumberZp32.ONE, ring);
            System.out.println("Matrix B = " + B);

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
            MPITransport.sendObjectArray
                    (new Object[]{AA[3], BB[0]}, 0, 2, 8, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[3], BB[2]}, 0, 2, 9, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[3], BB[1]}, 0, 2, 10, tag);     
            MPITransport.sendObjectArray
                    (new Object[]{AA[3], BB[3]}, 0, 2, 11, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[3], BB[0]}, 0, 2, 12, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[3], BB[2]}, 0, 2, 13, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[3], BB[1]}, 0, 2, 14, tag);
            MPITransport.sendObjectArray
                    (new Object[]{AA[3], BB[3]}, 0, 2, 15, tag);
                      
            MatrixS[] DD = new MatrixS[4];

            DD[0] = (AA[0].multiply(BB[0], ring)).add((MatrixS) MPITransport.recvObject(1, 3),ring);
            DD[1] = (MatrixS) MPITransport.recvObject(2, 3);
            DD[2] = (MatrixS) MPITransport.recvObject(4, 3);
            DD[3] = (MatrixS) MPITransport.recvObject(6, 3);

            D = MatrixS.join(DD);

            System.out.println("RESULT = " + D.toString());
        } 
        else {
            System.out.println("Processor №" + rank);

            Object[] b = new Object[4];

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


/*
command:
halynachemerys/openmpi/bin/mpirun --hostfile hostfile  -np 8 java -cp /home/finstereule/halynachemerys/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/chemerys/MatrixMult_Module2 3

output: 

Processor №3
Processor №5
Processor №6
Processor №4
Processor №2
Processor №7
Processor №1

Matrix A = 
[[7,  4,  11]
 [11, 29, 29]
 [20, 1,  30]]

Vector B = [7, 26, 20]

Rank = 0; row = [7, 4, 11]
Rank = 0; row = [11, 29, 29]
Rank = 0; row = [20, 1, 30]
Rank = 3 B = [7, 26, 20]
Rank = 1 B = [7, 26, 20]
Sending result...
Rank = 2 B = [7, 26, 20]
Sending result...
Sending result...
Rank = 5 B = [7, 26, 20]
Rank = 4 B = [7, 26, 20]
Sending result...
Rank = 7 B = [7, 26, 20]
Rank = 6 B = [7, 26, 20]
Sending result...
Sending result...
Sending result...

A * B = 
[[[49, 182, 140],
[28, 104, 80],
[77, 286, 220]], 
[[77, 286, 220],
[203, 754, 580],
[203, 754, 580]], 
[[140, 520, 400],
[7, 26, 20],
[210, 780, 600]]]
*/

class MultiplyMatrixToVector
{
    public static void multiplyMatrixToVector(String[] args)throws MPIException, IOException,ClassNotFoundException
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
            System.out.println("\nVector B = " + B + "\n");
            Element[] res0 = new Element[n];
            for (int i = 0; i < n; i++)
            {
                res0[i] = new VectorS(A.M[i]).transpose(ring).multiply(B, ring);
                System.out.println("Rank = " + rank + "; row = " + Array.toString(A.M[i]));
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
            System.out.println("\nA * B = " + new VectorS(result).toString(ring));
        }
        else
        {
            System.out.println("Processor №" + rank);
            Element[][] A = new Element[k][ord];
            for (int i = 0; i < k; i++)
            {
                A[i] = (Element[])MPITransport.recvObject(0, 100 + rank);
                System.out.println("Rank = " + rank + "; Row = " + Array.toString(A[i]));
            }
            Element[] B = (Element[])MPITransport.recvObject(0, 100 + rank);
            System.out.println("Rank = " + rank + " B = "+ Array.toString(B));
            Element[] result = new Element[k];
            for (int j = 0; j < A.length; j++)
                result[j] = new VectorS(A[j]).transpose(ring).multiply(new VectorS(B), ring);
            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("Sending result...");
        }
        MPI.Finalize();
    }
}

/*

command:
halynachemerys/openmpi/bin/mpirun --hostfile hostfile  -np 8 java -cp /home/finstereule/halynachemerys/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/chemerys/MatrixMult_Module2 4 3

output:
Processor №1
Processor №5
Processor №6
Processor №7
Processor №2
Processor №3
Processor №4

Vector B = [8, 20, 28, 12]
Rank = 6; B = []
Rank = 5; B = []

Sending result...

Sending result...
Rank = 3; B = []
Rank = 1; B = []
Rank = 7; B = []
Rank = 2; B = []

Sending result...

Sending result...

Sending result...
Rank = 4; B = []

Sending result...

Sending result...
B * S = [24, 60, 84, 36]
*/

class MultiplyVectorToScalar {
    static void multiplyVectorToScalar(String[] args)
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
                System.arraycopy(resRank, 0, result, n
                        + (t - 1) * k, resRank.length);
            }
            System.out.println("B * S = "
                    + new VectorS(result).toString(ring));
        } else {
            System.out.println("Processor №" + rank);
            Element[] B = (Element[]) MPITransport.recvObject(0, 100 + rank);
            System.out.println("Rank = " + rank + "; B = " + Array.toString(B));
            Element[] result = new Element[k];
            for (int j = 0; j < B.length; j++) {
                result[j] = B[j].multiply(s, ring);
            }
            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("\nSending result...");
        }
        MPI.Finalize();
    }
}
