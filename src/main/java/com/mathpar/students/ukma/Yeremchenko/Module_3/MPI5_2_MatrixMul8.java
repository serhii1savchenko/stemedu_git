package com.mathpar.students.ukma.Yeremchenko.Module_3;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import java.io.IOException;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

public class MPI5_2_MatrixMul8 {
    
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
Input:
mpirun -np 2 java -cp /home/igorko/homework/stemedu/target/classes  com/mathpar/students/ukma/Yeremchenko/Module_3/MPI5_2_MatrixMul8 2

Output:
I'm processor 1
A = 
[[0.47, 0.64, 0.44, 0.42]
 [0.8,  0.35, 0.57, 0.22]
 [0.4,  0.57, 0.25, 0.09]
 [0.9,  0.35, 0.84, 0.1 ]]
B = 
[[0.79, 0.67, 0.63, 0.82]
 [0.7,  0.41, 0.54, 0.71]
 [0.2,  0.56, 0.4,  0.76]
 [0.76, 0.92, 0.01, 0.82]]

*/