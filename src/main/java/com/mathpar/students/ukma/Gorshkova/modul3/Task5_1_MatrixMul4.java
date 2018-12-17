package com.mathpar.students.ukma.Gorshkova.modul3;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import java.io.IOException;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

public class Task5_1_MatrixMul4 {
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
            System.out.println("recv 1 to 0");
            DD[2] = (MatrixS) MPITransport.recvObject(2, 2);
            System.out.println("recv 2 to 0");
            DD[3] = (MatrixS) MPITransport.recvObject(3, 3);
            System.out.println("recv 3 to 0");
            CC = MatrixS.join(DD);
            System.out.println("RES= " + CC.toString());
       } else {
            System.out.println("I'm processor " + rank);
            Object[] n = new Object[4];
            MPITransport.recvObjectArray(n,0,4, 0, rank);
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

/*
cd 
mpijavac -cp 
mpirun --hostfile 


I'm processor 1
I'm processor 3
I'm processor 2
res = 
[[0.62, 0.58]
 [1.07, 0.86]]
res = 
[[0.83, 0.75]
 [1.4,  0.99]]
res = 
[[0.79, 1.08]
 [1.17, 1.66]]
send result
recv 1 to 0
send result
send result
recv 2 to 0
recv 3 to 0
RES= 
[[0.9,  1.07, 0.62, 0.58]
 [1.04, 1.37, 1.07, 0.86]
 [0.79, 1.08, 0.83, 0.75]
 [1.17, 1.66, 1.4,  0.99]]
+*/