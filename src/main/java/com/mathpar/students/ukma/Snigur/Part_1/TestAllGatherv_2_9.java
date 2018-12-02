package com.mathpar.students.ukma.Snigur.Part_1;
import mpi.MPI;
import mpi.MPIException;

public class TestAllGatherv_2_9 {

    public static void main(String[] args) throws MPIException, InterruptedException {
        // èíèöèàëèçàöèÿ MPI
        MPI.Init(args);
        //îïðåäåëåíèå íîìåðà ïðîöåññîðà
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        int[] q = new int[n * np];
        MPI.COMM_WORLD.allGatherv(a, n, MPI.INT, q, new int[]{n, n}, new int[]{0, 2}, MPI.INT);

            Thread.sleep(60 * myrank);
            System.out.println("myrank " + myrank + " : ");
            if (myrank == np - 1) {
                for (int i = 0; i < q.length; i++) {
                    System.out.println(" " + q[i]);
                }
            }
            System.out.println();
            //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
            MPI.Finalize();
        }
    }


// Output:
//        myrank 0 :
//
//        myrank 1 :
//        0
//        1
//        0
//        1
