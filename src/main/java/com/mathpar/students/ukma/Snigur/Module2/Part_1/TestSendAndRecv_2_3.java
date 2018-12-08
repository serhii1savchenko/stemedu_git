package com.mathpar.students.ukma.Snigur.Module2.Part_1;
import java.util.Random;
import mpi.*;
    public class TestSendAndRecv_2_3 {
        public static void main(String[] args)
            throws MPIException {
            //èíèöèàëèçàöèÿ MPI
            MPI.Init(args);
            //îïðåäåëåíèå íîìåðà ïðöåññîðà
            int myrank = MPI.COMM_WORLD.getRank();
            //îïðåäåëåíèå ÷èñëà ïðîöåññîðîâ â ãðóïïå
            int np = MPI.COMM_WORLD.getSize();
            //âõîäíîé ïàðàìåòð  ðàçìåð ìàññèâà
            int n = Integer.parseInt(args[0]);
            double[] a = new double[n];
            //ñèíõðîíèçàöèÿ ïðîöåññîðîâ
            MPI.COMM_WORLD.barrier();
            //åñëè ïðîöåññîð ñ íîìåðîì 0
            if (myrank == 0) {
                for (int i = 0; i < n; i++) {
                    a[i] = (new Random()).nextDouble();
                    System.out.println("a[" + i + "]= " + a[i]);
                }
                //ïåðåäà÷à 0-ïðîöåññîðîì ýëåìåíòîâ
                for (int i = 1; i < np; i++) {
                    MPI.COMM_WORLD.send(a, n, MPI.DOUBLE, i, 3000);
                }
                System.out.println("Proc num " + myrank + " Ìàññèâ îòïðàâëåí" + "\n");
            } else {
                //ïðè¼ì i-ì ïðîöåññîðîì ñîãîáùåíèÿ îò
                //ïðîöåññîðà ñ íîìåðîì 0.
                MPI.COMM_WORLD.recv(a, n, MPI.DOUBLE, 0, 3000);
                for (int i = 0; i < n; i++) {
                    System.out.println("a[" + i + "]= " + a[i]);
                }
                System.out.println("Proc num " + myrank + "Ìàññèâ ïðèíÿò" + "\n");
            }
            //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
            MPI.Finalize();
        }
    }
//mpirun java TestSendAndRecv_2_3
// Output for amount of processors equals 4
//        proc num = 0 array was sent
//        proc num = 1 array was received
//        proc num = 3 array was received
//        proc num = 2 array was received