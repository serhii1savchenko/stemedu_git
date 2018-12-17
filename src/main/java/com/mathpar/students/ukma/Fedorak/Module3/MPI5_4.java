package com.mathpar.students.ukma.Fedorak.Module3;

import com.mathpar.number.Array;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.parallel.utils.MPITransport;
import java.io.IOException;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

public class MPI5_4 {
  
    
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
            for (int t = 1; t < size; t++) {
                Element[] resRank = (Element[])
                MPITransport.recvObject(t, 100 + t);
                System.arraycopy(resRank, 0, result, n + (t - 1) * k, resRank.length);
            }
            System.out.println("B * S = " +
                new VectorS(result).toString(ring));
        } else {
            System.out.println("I'm processor " + rank);
            Element[] B = (Element[])
            MPITransport.recvObject(0, 100 + rank);
            System.out.println("rank = " + rank +
                " B = " + Array.toString(B));
            Element[] result = new Element[k];
            for (int j = 0; j < B.length; j++)
                result[j] = B[j].multiply(s, ring);
            MPITransport.sendObject(result, 0, 100 + rank);
            System.out.println("send result");
        }
        MPI.Finalize();
    }
}

/*
mpirun -np 8 --hostfile hostfile java -cp /home/bogdan/MPI/stemedu/stemedu/target/classes/ com.mathpar.students.ukma.Fedorak.Module3.MPI5_4 8 4

I'm processor 4
I'm processor 6
I'm processor 1I'm processor 3

I'm processor 2
Vector B = [0, 13, 31, 14, 19, 3, 21, 20]
I'm processor 7
I'm processor 5
rank = 1 B = [13]
rank = 6 B = [21]
rank = 4 B = [19]send result

send result
rank = 5 B = [3]
send result
rank = 3 B = [14]
send result
rank = 7 B = [20]
send result
rank = 2 B = [31]
send result
send result
B * S = [0, 52, 124, 56, 76, 12, 84, 80]

*/
