package com.mathpar.students.ukma.Zadorozhnyi.Module2.Part_1;
import java.math.BigInteger;
import java.util.Random;
import mpi.*;
//
/**
* Ïðîöåññîð ñ íîìåðîì 0 ïåðåñûëàåò ìàññèâ ÷èñåë
* îñòàëüíûì ïðîöåññîðàì,
* èñïîëüçóÿ ðàçäà÷ó ïî áèíàðíîìó äåðåâó.
*/
public class TestBcast_2_5 {
    public static void main(String[] args) throws MPIException {
        // èíèöèàëèçàöèÿ MPI
        MPI.Init(args);
        //îïðåäåëåíèå íîìåðà ïðîöåññîðà
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        double[] a = new double[n];
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        MPI.COMM_WORLD.barrier();
        // ïåðåäà÷à äàííûõ îò 0 ïðîöåññîðà îñòàëüíûì
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0) {
            for (int i = 0; i < n; i++) {
                System.out.println("a[" + i + "]= " + a[i]);
            }
        }
        //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
        MPI.Finalize();
    }
}
//mpirun java TestBcast_2_5
// Output:
//        a[0]= 0.44425017395071087
//        a[1]= 0.7786018081759669
//        a[2]= 0.7845693804372024
//        a[3]= 0.12285034656396099
//        a[0]= 0.44425017395071087
//        a[1]= 0.7786018081759669
//        a[2]= 0.7845693804372024
//        a[3]= 0.12285034656396099
//        a[0]= 0.44425017395071087
//        a[1]= 0.7786018081759669
//        a[2]= 0.7845693804372024
//        a[3]= 0.12285034656396099
//        a[0]= 0.44425017395071087
//        a[1]= 0.7786018081759669
//        a[2]= 0.7845693804372024
//        a[3]= 0.12285034656396099