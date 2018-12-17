package com.mathpar.students.ukma.Fedorak.Module3;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import java.io.IOException;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

public class MPI5_2 {
    
    public static void main(String[] args)
    throws MPIException, IOException, ClassNotFoundException {
        Ring ring = new Ring("R64[x]");
        MPI.Init(new String[0]);
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == 0) {
            int ord = 4;
            int den = 10000;
            Random rnd = new Random();
            MatrixS A = new MatrixS(ord, ord, den,
                new int[] {5, 3}, rnd, NumberZp32.ONE, ring);
            System.out.println("A = " + A);
            MatrixS B = new MatrixS(ord, ord, den,
                new int[] {5, 3}, rnd, NumberZp32.ONE, ring);
            System.out.println("B = " + B);
            MatrixS D = null;
            MatrixS[] AA = A.split();
            MatrixS[] BB = B.split();
            int tag = 0;
            MPITransport.sendObjectArray (new Object[] {AA[1], BB[2]},0,2, 1, tag);
            MPITransport.sendObjectArray (new Object[] {AA[0], BB[1]},0,2, 2, tag);
            MPITransport.sendObjectArray (new Object[] {AA[1], BB[3]},0,2, 3, tag);
            MPITransport.sendObjectArray (new Object[] {AA[2], BB[0]},0,2, 4, tag);
            MPITransport.sendObjectArray (new Object[] {AA[3], BB[2]},0,2, 5, tag);
            MPITransport.sendObjectArray (new Object[] {AA[2], BB[1]},0,2, 6, tag);
            MPITransport.sendObjectArray (new Object[] {AA[3], BB[3]},0,2, 7, tag);
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
            MPITransport.recvObjectArray(b,0,2,0, 0);
            MatrixS[] a = new MatrixS[b.length];
            for (int i = 0; i < b.length; i++)
                a[i] = (MatrixS) b[i];
            MatrixS res = a[0].multiply(a[1], ring);
            if (rank % 2 == 0) {
                MatrixS p = res.add((MatrixS) MPITransport.recvObject(rank + 1, 3), ring);
                MPITransport.sendObject(p, 0, 3);
            } else {
                MPITransport.sendObject(res, rank - 1, 3);
            }
        }
        MPI.Finalize();
    }
}

/*

 mpirun -np 16 --hostfile hostfile java -cp /home/bogdan/MPI/stemedu/stemedu/target/classes/ com.mathpar.students.ukma.Fedorak.Module3.MPI5_2

I'm processor 12I'm processor 10
I'm processor 6I'm processor 2
I'm processor 5

I'm processor 1I'm processor 14

I'm processor 3

I'm processor 8
I'm processor 11
I'm processor 4
I'm processor 13
I'm processor 15
A = 
[[0.72, 0.48, 0.6,  0.37]
 [0.23, 0.31, 0.73, 0.72]
 [0.6,  0.54, 0.87, 0.47]
 [0.43, 0.21, 0.83, 0.12]]
B = 
[[0.36, 0.46, 0.88, 0.45]
 [0.34, 0.61, 0.3,  0.14]
 [0.33, 0.78, 0.72, 0.95]
 [0.16, 0.13, 0.63, 0.18]]
I'm processor 7
I'm processor 9
RES = 
[[0.68, 1.14, 1.44, 1.03]
 [0.54, 0.96, 1.27, 0.97]
 [0.76, 1.35, 1.62, 1.26]
 [0.52, 0.99, 1.11, 1.03]]


*/
