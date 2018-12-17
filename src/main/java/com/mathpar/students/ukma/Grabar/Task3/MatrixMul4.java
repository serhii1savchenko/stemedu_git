package com.mathpar.students.ukma.Grabar.Task3;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import java.io.IOException;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

public class MatrixMul4 {
    static int tag = 0;
    static int mod = 13;
    
    public static MatrixS mmultiply(MatrixS a, MatrixS b,
        MatrixS c, MatrixS d, Ring ring) {
            return (a.multiply(b, ring)).add(c.multiply(d, ring), ring);
    }
    public static void main(String[] args)
    throws MPIException, IOException, ClassNotFoundException {
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
            AA[0], BB[1], AA[1], BB[3]},0,4, 1, 1);
            MPITransport.sendObjectArray(new Object[] {
            AA[2], BB[0], AA[3], BB[2]},0,4, 2, 2);
            MPITransport.sendObjectArray(new Object[] {
            AA[2], BB[1], AA[3], BB[3]},0,4, 3, 3);
            DD[0] = (AA[0].multiply(BB[0], ring)).
            add(AA[1].multiply(BB[2], ring), ring);
            DD[1] = (MatrixS) MPITransport.recvObject(1, 1);
            System.out.println("recv 0 from 1");
            DD[2] = (MatrixS) MPITransport.recvObject(2, 2);
            System.out.println("recv 0 from 2");
            DD[3] = (MatrixS) MPITransport.recvObject(3, 3);
            System.out.println("recv 0 from 3");
            CC = MatrixS.join(DD);
            System.out.println("res CC: " + CC.toString());
        } else {
            System.out.println("Current processor: " + rank);
            Object[] n = new Object[4];
            MPITransport.recvObjectArray(n,0,4, 0, rank);
            MatrixS a = (MatrixS) n[0];
            MatrixS b = (MatrixS) n[1];
            MatrixS c = (MatrixS) n[2];
            MatrixS d = (MatrixS) n[3];
            MatrixS res = mmultiply(a, b, c, d, ring);
            System.out.println("res: " + res);
            MPITransport.sendObject(res, 0, rank);
            System.out.println("Send result!");
        }
        MPI.Finalize();
    }
}

/*
Input:
mpirun -np 4 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Grabar/Task3/MatrixMul4

Output:
Current processor: 2
Current processor: 1
Current processor: 3
res: 
[[0.76, 0.57]
 [1.88, 1.41]]
res: 
[[0.73, 0.46]
 [1.86, 1.22]]
Send result!
Send result!
res: 
[[1.1,  0.64]
 [1.16, 0.71]]
Send result!
recv 0 from 1
recv 0 from 2
recv 0 from 3
res CC: 
[[1.04, 0.79, 1.1,  0.64]
 [1.25, 0.94, 1.16, 0.71]
 [0.76, 0.57, 0.73, 0.46]
 [1.88, 1.41, 1.86, 1.22]]

*/