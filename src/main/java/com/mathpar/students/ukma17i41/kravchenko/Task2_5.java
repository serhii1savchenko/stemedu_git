/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.kravchenko;

import java.io.IOException;
import mpi.MPIException;
import com.mathpar.matrix.*;
import mpi.*;
import com.mathpar.number.*;
import com.mathpar.parallel.utils.MPITransport;
import java.util.Random;

/**
 *
 * @author kravchenko
 */
public class Task2_5 {
      
    public static void main(String[] args)
            throws MPIException, IOException, ClassNotFoundException {
        
        /*run
         openmpi/bin/mpirun --default-hostfile none  -np 10 java -cp /home/kravchenko/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task2_5
        */
        
        //MatrixMul4.multiplyMatrixes(args);
        
        /*output
        I'm processor 3
        I'm processor 5
        I'm processor 2
        I'm processor 1
        I'm processor 4
        res =
        [[1.79, 1.05]
        [0.89, 0.63]]
        send result
        res = 
        [[1.19, 0.62]
        [1.68, 1.32]]
        send result
        res = 
        [[0.89, 0.3 ]
        [1.87, 0.61]]
        send result
        recv 1 to 0
        recv 2 to 0
        recv 3 to 0
        RES= 
        [[1.98, 1.68, 1.79, 1.05]
        [1.74, 0.87, 0.89, 0.63]
        [1.19, 0.62, 0.89, 0.3 ]
        [1.68, 1.32, 1.87, 0.61]]
        */
        
        /*run
         openmpi/bin/mpirun --default-hostfile none  -np 10 java -cp /home/kravchenko/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task2_5
        */
        //MatrixMul8.main(args);
        /*output
        I'm processor 1
        I'm processor 3
        I'm processor 6
        I'm processor 9
        I'm processor 4
        I'm processor 2
        I'm processor 7
        I'm processor 5
        I'm processor 8
        
        A = 
        [[0.92, 0.27, 0.2,  0.22]
        [0.4,  0.66, 0.64, 0.26]
        [0.79, 0.69, 0.43, 0.49]
        [0.17, 0.98, 0.95, 0.2 ]]
        B = 
        [[0.07, 0.8,  0.38, 0.29]
        [0.22, 0.5,  0.66, 0.73]
        [0.27, 0.75, 0.79, 0.06]
        [0.02, 0.54, 0.82, 0.25]]
        !!3
        RES = 
        [[0.18, 1.14, 0.87, 0.53]
        [0.35, 1.26, 1.3,  0.69]
        [0.33, 1.56, 1.49, 0.87]
        [0.49, 1.45, 1.62, 0.87]]
        */
        
         /*run
         openmpi/bin/mpirun --default-hostfile none  -np 10 java -cp /home/kravchenko/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task2_5 2
        */
        //MultiplyMatrixToVector.main(args);
        /*output
        I'm processor 1
        I'm processor 2
        I'm processor 4
        I'm processor 5
        I'm processor 3
        I'm processor 6
        I'm processor 9
        I'm processor 8
        I'm processor 7
        
        Matrix A = [[2,  17]
        [11, 29]]
        Vector B = [21, 27]
        rank = 0 row = [2, 17]
        rank = 0 row = [11, 29]
        rank = 1 B = [21, 27]
        rank = 2 B = [21, 27]
        rank = 8 B = [21, 27]
        send result
        rank = 4 B = [21, 27]
        send result
        rank = 7 B = [21, 27]
        send result
        send result
        send result
        rank = 9 B = [21, 27]
        rank = 5 B = [21, 27]
        rank = 3 B = [21, 27]
        send result
        rank = 6 B = [21, 27]
        send result
        send result
        send result
        A * B = [null, null]
        */
        
        /*run
         openmpi/bin/mpirun --default-hostfile none  -np 10 java -cp /home/kravchenko/stemedu/target/classes com/mathpar/students/ukma17i41/kravchenko/Task2_5 3 3
        */
        MultiplyVectorToScalar.main(args);
        /*output
        I'm processor 8
        I'm processor 7
        I'm processor 4
        I'm processor 9
        I'm processor 1
        I'm processor 3
        I'm processor 5
        I'm processor 6
        I'm processor 2
        
        
        Vector B = [25, 19, 15]
        rank = 9 B = []
        rank = 1 B = []
        rank = 4 B = []
        send result
        send result
        rank = 8 B = []
        send result
        send result
        rank = 7 B = []
        rank = 5 B = []
        rank = 2 B = []
        rank = 6 B = []
        rank = 3 B = []
        send result
        send result
        send result
        send result
        send result
        B * S = [75, 57, 45]
        */
    }
    
}

 class MatrixMul4 {
     
     static int tag = 0;
     static int mod = 13;
     
     public static MatrixS mmultiply(MatrixS a, MatrixS b,MatrixS c, MatrixS d, Ring ring) {
         return (a.multiply(b, ring)).add(c.multiply(d, ring), ring);
     }
     
      public static void multiplyMatrixes(String[] args)
            throws MPIException, IOException,ClassNotFoundException {
          
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

            DD[0] = (AA[0].multiply(BB[0], ring)).add(AA[1].multiply(BB[2], ring), ring);
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
            
            MPITransport.sendObjectArray (new Object[] {AA[1], BB[2]},0, 2, 1, tag);
            MPITransport.sendObjectArray (new Object[] {AA[0], BB[1]},0, 2, 2, tag);
            MPITransport.sendObjectArray (new Object[] {AA[1], BB[3]},0, 2, 3, tag);
            MPITransport.sendObjectArray (new Object[] {AA[2], BB[0]},0, 2, 4, tag);
            MPITransport.sendObjectArray (new Object[] {AA[3], BB[2]},0, 2, 5, tag); 
            MPITransport.sendObjectArray (new Object[] {AA[2], BB[1]},0, 2, 6, tag);
            MPITransport.sendObjectArray (new Object[] {AA[3], BB[3]},0, 2, 7, tag);
                               
            MatrixS[] DD = new MatrixS[4];
            
            DD[0] = (AA[0].multiply(BB[0], ring)).add((MatrixS) MPITransport.recvObject(1, 3),ring);
            DD[1] = (MatrixS) MPITransport.recvObject(2, 3);
            DD[2] = (MatrixS) MPITransport.recvObject(4, 3);
            DD[3] = (MatrixS) MPITransport.recvObject(6, 3);
            
            D = MatrixS.join(DD);
            System.out.println("!!3");
            System.out.println("RES = " + D.toString());
        
        } else{
            
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

class MultiplyMatrixToVector {
    public static void main(String[] args)
            throws MPIException, IOException,ClassNotFoundException {
        Ring ring = new Ring("Z[x]");
        MPI.Init(args);
        
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();
        
        int ord = Integer.parseInt(args[0]);
        int k = ord / size;
        int n = ord - k * (size - 1);
        
        if (rank == 0) {
            int den = 10000;
            Random rnd = new Random();
            
            MatrixS A = new MatrixS(ord, ord, den,new int[] {5, 5}, rnd, NumberZp32.ONE, ring);
            System.out.println("Matrix A = " + A);
            
            VectorS B = new VectorS(ord, den, new int[] {5, 5}, rnd, ring);
            System.out.println("Vector B = " + B);
            
            Element[] res0 = new Element[n];
            
            for (int i = 0; i < n; i++) {
                res0[i] = new VectorS(A.M[i]).multiply(B, ring);
                System.out.println("rank = " + rank + " row = "+ Array.toString(A.M[i]));
            }
            
            for (int j = 1; j < size; j++) {
                for (int z = 0; z < k; z++)
                        MPITransport.sendObject(A.M[n + (j - 1) * k + j * z], j, 100 + j);
                
                MPITransport.sendObject(B.V, j, 100 + j);
            }
            
            Element[] result = new Element[ord];
            System.arraycopy(res0, 0, result, 0, n);
            
            for (int t = 1; t < size; t++) {
                Element[] resRank = (Element[])
                        MPITransport.recvObject(t, 100 + t);
                System.arraycopy(resRank, 0, result, n + (t - 1) * k, resRank.length);
            }
            
            System.out.println("A * B = " + new VectorS(result).toString(ring));
        
        } else {
            System.out.println("I'm processor " + rank);
            Element[][] A = new Element[k][ord];
            
            for (int i = 0; i < k; i++) {
                A[i] = (Element[])
                        MPITransport.recvObject(0, 100 + rank);
                System.out.println("rank = " + rank + "row = " + Array.toString(A[i]));
            }
            
            Element[] B = (Element[])
                    MPITransport.recvObject(0, 100 + rank);
            System.out.println("rank = " + rank + " B = " + Array.toString(B));
            
            Element[] result = new Element[k];
            
            for (int j = 0; j < A.length; j++)
                result[j] = new VectorS(A[j]).multiply(new VectorS(B), ring);
            
            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("send result");
        }
        MPI.Finalize();
    }
}

class MultiplyVectorToScalar {
    public static void main(String[] args)
            throws MPIException, IOException, ClassNotFoundException {
        
        Ring ring = new Ring("Z[x]");
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();
        
        int ord = Integer.parseInt(args[0]);
        
        Element s = NumberR64.valueOf(Integer.parseInt(args[1]));
        
        int k = ord / size;
        int n = ord - k * (size - 1);
        if (rank == 0) {
            int den = 10000;
            Random rnd = new Random();
            VectorS B = new VectorS(ord, den,new int[] {5, 5}, rnd, ring);
            
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
            
            for (int t = 1; t < size; t++) {
                Element[] resRank = (Element[])
                        MPITransport.recvObject(t, 100 + t);
                System.arraycopy(resRank, 0, result, n +
                        (t - 1) * k, resRank.length);
            }
            
            System.out.println("B * S = " + new VectorS(result).toString(ring));
        } else {
          
            System.out.println("I'm processor " + rank);
            Element[] B = (Element[])
                    MPITransport.recvObject(0, 100 + rank);
            
            System.out.println("rank = " + rank + " B = " + Array.toString(B));
            
            Element[] result = new Element[k];
            
            for (int j = 0; j < B.length; j++)
                    result[j] = B[j].multiply(s, ring);
            
            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("send result");
        }
        MPI.Finalize();
    }
}