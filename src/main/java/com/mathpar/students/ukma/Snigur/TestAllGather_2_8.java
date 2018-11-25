import mpi.*;

public class TestAllGather_2_8 {
    public static void main(String[] args) throws Exception {
        // èíèöèàëèçàöèÿ MPI
        MPI.Init(args);
        Thread t = new Thread();
        //îïðåäåëåíèå íîìåðà ïðîöåññîðà
        int myrank = MPI.COMM_WORLD.getRank();
        // îïðåäåëåíèå ÷èñëà ïðîöåñññîâ â ãðóïïå
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = myrank;
        }
        int[] q = new int[n * np];
        MPI.COMM_WORLD.allGather(a, n, MPI.INT, q, n, MPI.INT);
        t.sleep(60 * myrank);
        System.out.println("myrank = " + myrank + " : ");
        for (int i = 0; i < q.length; i++) {
            System.out.println(" " + q[i]);
        }
        System.out.println();
        //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
        MPI.Finalize();
    }
}

// Output:
//        myrank = 0 :
//        0
//        0
//        0
//        0
//        1
//        1
//        1
//        1
//
//        myrank = 1 :
//        0
//        0
//        0
//        0
//        1
//        1
//        1
//        1
