/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Lavrenchuk.Module3;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import java.io.IOException;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;


public class MatrixMul8 {
public static void main(String[] args)throws MPIException, IOException,ClassNotFoundException {
Ring ring = new Ring("R64[x]");
// èíèöèàëèçàöèÿ MPI
MPI.Init(new String[0]);
// ïîëó÷åíèå íîìåðà ïðîöåññîðà
int rank = MPI.COMM_WORLD.getRank();
if (rank == 0) {
// ïðîãðàììà âûïîëíÿåòñÿ íà íóëåâîì ïðîöåññîðå
int ord = 4;
int den = 10000;
// ïðåäñòàâèòåëü êëàññà ñëó÷àéíîãî ãåíåðàòîðà
Random rnd = new Random();
// ord = ðàçìåð ìàòðèöû, den = ïëîòíîñòü
MatrixS A = new MatrixS(ord, ord, den,
new int[] {5, 3}, rnd, NumberZp32.ONE, ring);
System.out.println("A = " + A);
MatrixS B = new MatrixS(ord, ord, den,
new int[] {5, 3}, rnd, NumberZp32.ONE, ring);
System.out.println("B = " + B);
MatrixS D = null;
// ðàçáèâàåì ìàòðèöó A íà 4 ÷àñòè
MatrixS[] AA = A.split();
// ðàçáèâàåì ìàòðèöó B íà 4 ÷àñòè
MatrixS[] BB = B.split();
int tag = 0;
// îòïðàâêà îò íóëåâîãî ïðîöåññîðà ìàññèâà Object
// ïðîöåññîðó rank ñ èäåíòèôèêàòîðîì tag
MPITransport.sendObjectArray
(new Object[] {AA[1], BB[2]},0,2, 1, tag);
MPITransport.sendObjectArray
(new Object[] {AA[0], BB[1]},0,2, 2, tag);
MPITransport.sendObjectArray
(new Object[] {AA[1], BB[3]},0,2, 3, tag);
MPITransport.sendObjectArray
(new Object[] {AA[2], BB[0]},0,2, 4, tag);
MPITransport.sendObjectArray
(new Object[] {AA[3], BB[2]},0,2, 5, tag);
MPITransport.sendObjectArray
(new Object[] {AA[2], BB[1]},0,2, 6, tag);
MPITransport.sendObjectArray
(new Object[] {AA[3], BB[3]},0,2, 7, tag);
MatrixS[] DD = new MatrixS[4];
// îñòàâëÿåì îäèí áëîê
//íóëåâîìó ïðîöåññîðó äëÿ îáðàáîòêè
DD[0] = (AA[0].multiply(BB[0], ring)).
add((MatrixS) MPITransport.recvObject(1, 3),
ring);
DD[1] = (MatrixS) MPITransport.recvObject(2, 3);
DD[2] = (MatrixS) MPITransport.recvObject(4, 3);
DD[3] = (MatrixS) MPITransport.recvObject(6, 3);
D = MatrixS.join(DD);
System.out.println("RES = " + D.toString());
} else {
// ïðîãðàììà âûïîëíÿåòñÿ íà ïðîöåññîðå
// ñ íîìåðîì rank
System.out.println("I'm processor " + rank);
Object[] b = new Object[2];
MPITransport.recvObjectArray(b,0,2,0, 0);
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
mpirun -np 2 java -cp /home/max/stemedu/target/classes com.mathpar.students.ukma.Snigur.Module3.MatrixMul8

I'm processor 1
A = 
[[0.99, 0.39, 0.26, 0.18]
 [0.71, 0.6,  0.5,  0.19]
 [0.47, 0.34, 0.68, 0.47]
 [1,    0.73, 0.09, 0.56]]
B = 
[[0.78, 0.1,  0.16, 0.36]
 [0.11, 0.13, 0.47, 0.85]
 [0.58, 0.9,  0.08, 0.62]
 [0.46, 0.56, 0.22, 0.8 ]]
*/