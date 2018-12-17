package com.mathpar.students.ukma.Zadorozhnyi.Module2.Part_1;
import java.nio.IntBuffer;
import java.util.Random;
import mpi.*;

public class TestISendandIRecv_2_4 {
    public static void main(String[] args) throws MPIException {
        // èíèöèàëèçàöèÿ MPI
        MPI.Init(args);
        // îïðåäåëåíèå íîìåðà ïðîöåññîðà
        int myrank = MPI.COMM_WORLD.getRank();
        // îïðåäåëåíèå ÷èñëà ïðîöåñññîâ â ãðóïïå
        int np = MPI.COMM_WORLD.getSize();
        //âõîäíîé ïàðàìåòð  ðàçìåð ìàññèâà
        int n = Integer.parseInt(args[0]);
        IntBuffer b = MPI.newIntBuffer(n);
        MPI.COMM_WORLD.barrier();
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                b.put(new Random().nextInt(10));
            }
            for (int i = 1; i < np; i++) {
                MPI.COMM_WORLD.iSend(b, b.capacity(), MPI.INT, i, 3000);
            }
            System.out.println("proc num = " + myrank +" array was sent");
        } else {
            MPI.COMM_WORLD.recv(b, b.capacity(), MPI.INT, 0, 3000);
            System.out.println("proc num = " + myrank + " array was received");
        }
        //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
        MPI.Finalize();
    }
}
//mpirun java TestISendandIRecv_2_4
// Output:
//        proc num = 0 array was sent
//        proc num = 1 array was received
//        proc num = 3 array was received
//        proc num = 2 array was received