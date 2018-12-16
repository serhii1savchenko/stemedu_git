/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Snigur.Module3;

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


public class MultiplyVectorToScalar {
public static void main(String[] args)throws MPIException, IOException,ClassNotFoundException {
Ring ring = new Ring("Z[x]");
MPI.Init(args);
int rank = MPI.COMM_WORLD.getRank();
int size = MPI.COMM_WORLD.getSize();
//ðàçìåð âåêòîðà
int ord = Integer.parseInt(args[0]);
//÷èñëî
Element s = NumberR64.valueOf(
Integer.parseInt(args[1]));
//êîëè÷åñòâî ýëåìåíòîâ äëÿ ïðîöåññîðîâ c íîìåðîì > 0
int k = ord / size;
//êîëè÷åñòâî ýëåìåíòîâ âåêòîðà
//äëÿ ïðîöåññîðà ñ íîìåðîì 0
int n = ord - k * (size - 1);
if (rank == 0) {
int den = 10000;
Random rnd = new Random();
VectorS B = new VectorS(ord, den,
new int[] {5, 5}, rnd, ring);
System.out.println("Vector B = " + B);
//ñîçäàåì ìàññèâ íà ïðîöåññîðå ñ íîìåðîì 0
Element[] res0 = new Element[n];
for (int i = 0; i < n; i++)
res0[i] = B.V[i].multiply(s, ring);
//îòïðàâêà ýëåìåíòîâ âåêòîðà
for (int j = 1; j < size; j++) {
Element[] v = new Element[k];
System.arraycopy(B.V, n + (j - 1) * k, v, 0, k);
MPITransport.sendObject(v, j, 100 + j);
}
//ìàññèâ, ñîäåðæàùèé ðåçóëüòàò
Element[] result = new Element[ord];
System.arraycopy(res0, 0, result, 0, n);
//ïðèíèìàåì ðåçóëüòàòû îò êàæäîãî ïðîöåññîðà
for (int t = 1; t < size; t++) {
Element[] resRank = (Element[])
MPITransport.recvObject(t, 100 + t);
System.arraycopy(resRank, 0, result, n + (t - 1) * k, resRank.length);
}
System.out.println("B * S = " +
new VectorS(result).toString(ring));
} else {
//ïðîãðàììà âûïîëíÿåòñÿ íà ïðîöåññîðå
//ñ íîìåðîì rank
System.out.println("I'm processor " + rank);
//ïðèíèìàåì ÷àñòü âåêòîðà B îò ïðîöåññîðà
//ñ íîìåðîì 0
Element[] B = (Element[])
MPITransport.recvObject(0, 100 + rank);
System.out.println("rank = " + rank +" B = " + Array.toString(B));
//ñîçäàíèå ìàññèâà äëÿ ðåçóëüòàòà óìíîæåíèÿ
//âåêòîðà íà ñêàëÿð
Element[] result = new Element[k];
for (int j = 0; j < B.length; j++)
result[j] = B[j].multiply(s, ring);
//îòïðàâêà ðåçóëüòàòà ïðîöåññîðó ñ íîìåðîì 0
MPITransport.sendObject(result, 0, 100 + rank);
System.out.println("send result");
}
MPI.Finalize();
}
}

/*
mpirun -np 2 java -cp /home/max/stemedu/target/classes com.mathpar.students.ukma.Snigur.Module3.MultiplyVectorToScalar

I'm processor 1
Vector B = [3, 23]
rank = 1 B = [23]
send result
B * S = [9, 69]
*/