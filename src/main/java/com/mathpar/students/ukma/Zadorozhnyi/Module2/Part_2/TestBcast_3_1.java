package com.mathpar.students.ukma.Zadorozhnyi.Module2.Part_2;
import java.util.Arrays;
import java.math.BigInteger;
import java.util.Random;
import mpi.*;
/* Ïðîöåññîð ñ íîìåðîì 0 ïåðåñûëàåò ìàññèâ ÷èñåë îñòàëüíûì ïðîöåññîðàì êîììóíèêàòîðà MPI.COMM_WORLD */
public class TestBcast_3_1 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        double[] a = new double[n];
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextDouble();
            }
            System.out.println("myrank = " + myrank + " : a = "
                    + Arrays.toString(a));
        }
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.bcast(a, a.length, MPI.DOUBLE, 0);
        if (myrank != 0)
            System.out.println("myrank = " + myrank + " : a = "
                    + Arrays.toString(a));
        MPI.Finalize();
    }
}
//mpirun java TestBcast_3_1
// Output:
//        myrank = 0 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
//        myrank = 3 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
//        myrank = 2 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
//        myrank = 1 : a = [0.1320077635416097, 0.9476896586791758, 0.32296007767227797, 0.16813548383931642]
