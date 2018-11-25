import mpi.*;

public class TestScatterv_2_11 {

        public static void main(String[] args) throws MPIException {
            // èíèöèàëèçàöèÿ MPI
            MPI.Init(args);
            //îïðåäåëåíèå íîìåðà ïðîöåññîðà
            int myrank = MPI.COMM_WORLD.getRank();
            int n = Integer.parseInt(args[0]);
            // îïðåäåëåíèå ÷èñëà ïðîöåñññîâ â ãðóïïå
            int[] a = new int[n];
            // îáúÿâëÿåì ìàññèâ öåëûõ ÷èñåë
            if (myrank == 0) {
                for (int i = 0; i < a.length; i++)
                    a[i] = i;
            }
            // îáúÿâëÿåì ìàññèâ, â êîòîðûé áóäóò çàïèñûâàòüñÿ
            // ïðèíÿòûå ïðîöåññîðîì ýëåìåíòû
            int[] q = new int[n];
            MPI.COMM_WORLD.barrier();
            MPI.COMM_WORLD.scatterv(a, new int[]{3, 2, 1, 1},
                    new int[]{0, 1, 2, 0}, MPI.INT, q, n, MPI.INT, 0);
            // ðàñïå÷àòûâàåì ïîëó÷åííâå ìàññèâû è íîìåðà ïðèíÿâøèõ
            // ïðîöåññîðîâ
            for (int i = 0; i < q.length; i++)
                System.out.print("myrank = " + myrank
                        + "; " + q[i] + "\n");
            //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
            MPI.Finalize();
        }
}

// Output:
//        myrank = 0; 0
//        myrank = 0; 1
//        myrank = 0; 2
//        myrank = 0; 0
//        myrank = 1; 1
//        myrank = 1; 2
//        myrank = 1; 11958677
//        myrank = 1; 0
