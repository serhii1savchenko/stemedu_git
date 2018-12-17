package com.mathpar.students.ukma.Zadorozhnyi.Module2.Part_1;
import mpi.MPI;
import mpi.MPIException;

public class TestScan_2_17 {
    public static void main(String[] args) throws MPIException {
        // èíèöèàëèçàöèÿ MPI
        MPI.Init(args);
        //îïðåäåëåíèå íîìåðà ïðîöåññîðà
        int myrank = MPI.COMM_WORLD.getRank();
        // îïðåäåëåíèå ÷èñëà ïðîöåñññîâ â ãðóïïå
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        int[] q = new int[n];
        MPI.COMM_WORLD.scan(a, q, n, MPI.INT, MPI.SUM);
        for (int j = 0; j < np; j++) {
            if (myrank == j) {
                System.out.println("myrank = " + j);
                for (int i = 0; i < q.length; i++) {
                    System.out.println(" " + q[i]);
                }
            }
        }
        //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
        MPI.Finalize();
    }
}
//mpirun java TestScan_2_17
// Output:
//myrank = 0
// 0
// 1
// 2
// 3
//myrank = 1
// 0
// 2
// 4
// 6

// Output:
//myrank = 0
// 0
// 1
//myrank = 1
// 0
// 2
//myrank = 3
// 0
// 4
//myrank = 2
// 0
// 3
