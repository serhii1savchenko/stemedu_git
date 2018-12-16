/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Vakulov.Module3;

import java.util.Random;
import com.mathpar.matrix.*;
import mpi.*;
import com.mathpar.number.*;
import com.mathpar.parallel.utils.MPITransport;
import java.io.IOException;

public class MatrixMul4 {
    static int tag = 0;
    static int mod = 13;
    
    public static MatrixS mmultiply(MatrixS a, MatrixS b,MatrixS c, MatrixS d, Ring ring) 
    {
    // óìíîæèì a íà b, ñ íà d è ñëîæèì ðåçóëüòàòû
        return (a.multiply(b, ring)).add(c.multiply(d, ring), ring);
    }
    public static void main(String[] args)throws MPIException, IOException,ClassNotFoundException 
    {
        Ring ring = new Ring("R64[x]");
        //èíèöèàëèçàöèÿ MPI
        MPI.Init(new String[0]);
        // ïîëó÷åíèå íîìåðà ïðîöåññîðà
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == 0) {
        // ïðîãðàììà âûïîëíÿåòñÿ íà íóëåâîì ïðîöåññîðå
        ring.setMOD32(mod);
        int ord = 4;
        int den = 10000;
    // ïðåäñòàâèòåëü êëàññà ñëó÷àéíîãî ãåíåðàòîðà
        Random rnd = new Random();
    // ord = ðàçìåð ìàòðèöû, den = ïëîòíîñòü
        MatrixS A = new MatrixS(ord, ord, den,
        new int[] {5, 5}, rnd, NumberZp32.ONE, ring);
        MatrixS B = new MatrixS(ord, ord, den,
        new int[] {5, 5}, rnd, NumberZp32.ONE, ring);
        MatrixS[] DD = new MatrixS[4];
        MatrixS CC = null;
// ðàçáèâàåì ìàòðèöó A íà 4 ÷àñòè
        MatrixS[] AA = A.split();
// ðàçáèâàåì ìàòðèöó B íà 4 ÷àñòè
        MatrixS[] BB = B.split();
// îòïðàâêà îò íóëåâîãî ïðîöåññîðà ìàññèâà Object
// ïðîöåññîðó 1 ñ èäåíòèôèêàòîðîì tag = 1
        MPITransport.sendObjectArray(new Object[] { AA[0], BB[1], AA[1], BB[3]},0,4, 1, 1);
// îòïðàâêà îò íóëåâîãî ïðîöåññîðà ìàññèâà Object
// ïðîöåññîðó 2 ñ èäåíòèôèêàòîðîì tag = 2
        MPITransport.sendObjectArray(new Object[]{ AA[2], BB[0], AA[3], BB[2]},0,4, 2, 2);
// îòïðàâêà îò íóëåâîãî ïðîöåññîðà ìàññèâà Object
// ïðîöåññîðó 3 ñ èäåíòèôèêàòîðîì tag = 3
        MPITransport.sendObjectArray(new Object[] {AA[2], BB[1], AA[3], BB[3]},0,4, 3, 3);
// îñòàâëÿåì îäèí áëîê íóëåâîìó ïðîöåññîðó äëÿ
// îáðàáîòêè
        DD[0] = (AA[0].multiply(BB[0], ring)).add(AA[1].multiply(BB[2], ring), ring);
// ïðèíèìàåì ðåçóëüòàò îò ïåðâîãî ïðîöåññîðà
        DD[1] = (MatrixS) MPITransport.recvObject(1, 1);
        System.out.println("recv 1 to 0");
// ïðèíèìàåì ðåçóëüòàò îò âòîðîãî ïðîöåññîðà
        DD[2] = (MatrixS) MPITransport.recvObject(2, 2);
        System.out.println("recv 2 to 0");
// ïðèíèìàåì ðåçóëüòàò îò òðåòüåãî ïðîöåññîðà
        DD[3] = (MatrixS) MPITransport.recvObject(3, 3);
        System.out.println("recv 3 to 0");
//ïðîöåäóðà ñáîðêè ìàòðèöû èç áëîêîâ DD[i]
//(i=0,...,3)
        CC = MatrixS.join(DD);
        System.out.println("RES= " + CC.toString());
        } else {
// ïðîãðàììà âûïîëíÿåòñÿ íà ïðîöåññîðå
// ñ íîìåðîì rank
        System.out.println("I'm processor " + rank);
        ring.setMOD32(mod);
// ïîëó÷àåì ìàññèâ Object ñ áëîêàìè ìàòðèö
// îò íóëåâîãî ïðîöåññîðà
        Object[] n = new Object[4];
        MPITransport.recvObjectArray(n,0,4, 0, rank);
        MatrixS a = (MatrixS) n[0];
        MatrixS b = (MatrixS) n[1];
        MatrixS c = (MatrixS) n[2];
        MatrixS d = (MatrixS) n[3];
// ïåðåìíîæàåì è ñêëàäûâàåì áëîêè ìàòðèö
        MatrixS res = mmultiply(a, b, c, d, ring);
// ïîñûëàåì ðåçóëüòàò âû÷èñëåíèé îò
// ïðîöåññîðà rank íóëåâîìó ïðîöåññîðó
        System.out.println("res = " + res);
        MPITransport.sendObject(res, 0, rank);
// ñîîáùåíèå íà êîíñîëü î òîì, ÷òî
// ðåçóëüòàò áóäåò ïîñëàí
        System.out.println("send result");
    }
    MPI.Finalize();
    }
}

/*
mpirun -np 4 java -cp /home/max/stemedu/target/classes com.mathpar.students.ukma.Snigur.Module3.MatrixMul4

I'm processor 1
I'm processor 2
I'm processor 3
res = 
[[0.89, 0.91]
 [1.11, 0.58]]
res = 
[[0.54, 0.38]
 [1.06, 0.95]]
res = 
[[0.54, 0.63]
 [1.49, 1.2 ]]
send result
recv 1 to 0
send result
send result
recv 2 to 0
recv 3 to 0
RES= 
[[0.86, 0.65, 0.89, 0.91]
 [0.93, 0.92, 1.11, 0.58]
 [0.54, 0.38, 0.54, 0.63]
 [1.06, 0.95, 1.49, 1.2 ]]
*/