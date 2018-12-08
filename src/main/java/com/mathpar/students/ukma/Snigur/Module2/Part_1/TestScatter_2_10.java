package com.mathpar.students.ukma.Snigur.Module2.Part_1;
import mpi.*;

public class TestScatter_2_10 {
    public static void main(String[] args) throws MPIException {
        // èíèöèàëèçàöèÿ MPI
        MPI.Init(args);
        //îïðåäåëåíèå íîìåðà ïðîöåññîðà
        int myrank = MPI.COMM_WORLD.getRank();
        // îïðåäåëåíèå ÷èñëà ïðîöåñññîâ â ãðóïïå
        int[] a = new int[2];
        // çàïîëíÿåì ýòîò ìàññèâ íà íóëåâîì ïðîöåññîðå
        if (myrank == 0) {
            for (int i = 0; i < 2; i++) {
                a[i] = i;
                System.out.println("a = " + a[i] + " ");
            }
            System.out.println("rank = " + myrank);
        }
        // îáúÿâëÿåì ìàññèâ, â êîòîðûé áóäóò çàïèñûâàòüñÿ
        // ïðèíÿòûå ïðîöåññîðîì ýëåìåíòû
        int[] q = new int[2];
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.scatter(a, 1, MPI.INT, q, 1, MPI.INT, 0);
        // ðàñïå÷àòûâàåì ïîëó÷åííûå ìàñññèâû è íîìåðà ïðîöåññîðîâ
        for (int i = 0; i < q.length; i++)
            System.out.println("myrank = " + myrank + " ; " + q[i] + "\n");
        //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
        MPI.Finalize();
    }
}
//mpirun java TestScatter_2_10
// Output:
//        a = 0
//        a = 1
//        rank = 0
//        myrank = 0 ; 0
//
//        myrank = 0 ; 0
//
//        myrank = 1 ; 1
//
//        myrank = 1 ; 0

