package com.mathpar.students.ukma.Snigur.Part_1;
import mpi.MPI;
import mpi.MPIException;

public class TestAllToAllv_2_13 {
    public static void main(String[] args) throws MPIException {
        // èíèöèàëèçàöèÿ MPI
        MPI.Init(args);
        //îïðåäåëåíèå íîìåðà ïðîöåññîðà
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.valueOf(i);
        }
        Integer[] b = new Integer[n];
        MPI.COMM_WORLD.allToAllv(a, new int[]{2, 2},
                new int[]{0, 2}, MPI.INT, b, new int[]{2, 2},
                new int[]{0, 2}, MPI.INT);
        for (int i = 0; i < 2; i++) {
            System.out.print("myrank = " + myrank
                    + "; " + b[i] + "\n");
        }
        //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
        MPI.Finalize();
    }
}


// Output for two processors
//        myrank = 0; 0
//        myrank = 0; 1
//        myrank = 1; 2
//        myrank = 1; 3