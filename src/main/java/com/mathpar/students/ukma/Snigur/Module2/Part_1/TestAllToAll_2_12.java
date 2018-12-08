package com.mathpar.students.ukma.Snigur.Module2.Part_1;
import java.nio.IntBuffer;
import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;

public class TestAllToAll_2_12 {

    public static void main(String[] args) throws MPIException {
        // èíèöèàëèçàöèÿ MPI
        MPI.Init(args);
        // îïðåäåëåíèå íîìåðà ïðîöåññîðà
        int myrank = MPI.COMM_WORLD.getRank();
        // îïðåäåëåíèå ÷èñëà ïðîöåñññîâ â ãðóïïå
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        //ñîçäàíèå ìàññèâà èç 4 ýëåìåíòîâ
        IntBuffer b = MPI.newIntBuffer(4);
        b.put(0); b.put(1); b.put(2); b.put(3);
        for (int i = 0; i < 4; i++)
            System.out.println("b[" + i + "] = " + b.get(i));
        System.out.println("capacity b = " + b.capacity());
        IntBuffer q = MPI.newIntBuffer(4);
        MPI.COMM_WORLD.allToAll(b, n, MPI.INT, q, n, MPI.INT);
        for (int i = 0; i < q.capacity(); i++) {
            System.out.println("myrank = " + myrank +
                    " send=" + b.get(i) + " recv=" + q.get(i));
        }
        //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
        MPI.Finalize();
    }
}
//mpirun java TestAllToAll_2_12
// Output:
//        b[0] = 0
//        b[1] = 1
//        b[2] = 2
//        b[3] = 3
//        capacity b = 4
//        b[0] = 0
//        b[1] = 1
//        b[2] = 2
//        b[3] = 3
//        capacity b = 4
//        myrank = 0 send=0 recv=0
//        myrank = 0 send=1 recv=1
//        myrank = 0 send=2 recv=0
//        myrank = 0 send=3 recv=1
//        myrank = 1 send=0 recv=2
//        myrank = 1 send=1 recv=3
//        myrank = 1 send=2 recv=2
//        myrank = 1 send=3 recv=3

