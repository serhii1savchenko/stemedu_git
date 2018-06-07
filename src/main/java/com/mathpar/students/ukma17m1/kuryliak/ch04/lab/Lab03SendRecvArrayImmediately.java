package com.mathpar.students.ukma17m1.kuryliak.ch04.lab;

import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Arrays;

import static com.mathpar.students.ukma17m1.kuryliak.ch04.util.SendRecvObject.*;

public class Lab03SendRecvArrayImmediately {

    // mpirun -np 2 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab03SendRecvArrayImmediately
    // mpirun -np 4 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab03SendRecvArrayImmediately
    // mpirun -np 5 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab03SendRecvArrayImmediately
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        if(rank % 2 == 0) {
            int recipient = rank+1;

            if (recipient >= size) {
                System.out.printf("[%d] not sending anything :(\n", rank);
            } else {
                int arrLen = recipient;
                MyObj[] arr = new MyObj[arrLen];
                for(int i = 0; i < arr.length; i++) {
                    arr[i] = MyObj.randomObject();
                }

                System.out.printf("[%d] sending %s...\n", rank, Arrays.toString(arr));
                sendObjects(arr, recipient, 0);
            }
        } else {
            int arrLen = rank;
            int sender = rank - 1;

            Object[] arr = recvObjects(arrLen, sender, 0);
            System.out.printf("[%d] received %s\n", rank, Arrays.toString(arr));
        }

        MPI.Finalize();
    }
    /*
    -np 2
    [0] sending [MyObj{intField=91, strField='lorem ipsum'}]...
    [1] received [MyObj{intField=91, strField='lorem ipsum'}]

    -np 4
    [[0] sending 2[MyObj{intField=92, strField='platonem no etiam'}]...
    ] sending [MyObj{intField=6, strField='nostrud'}, MyObj{intField=12, strField='integre vim nisl'}, MyObj{intField=41, strField='platonem no etiam'}]...
    [1] received [MyObj{intField=92, strField='platonem no etiam'}]
    [3] received [MyObj{intField=6, strField='nostrud'}, MyObj{intField=12, strField='integre vim nisl'}, MyObj{intField=41, strField='platonem no etiam'}]

    -np 5
    [2] sending [MyObj{intField=68, strField='nostrud'}, MyObj{intField=3, strField='per autem delenit'}, MyObj{intField=94, strField='dolor sit amet'}]...
    [4] not sending anything :(
    [0] sending [MyObj{intField=77, strField='lorem ipsum'}]...
    [3] received [MyObj{intField=68, strField='nostrud'}, MyObj{intField=3, strField='per autem delenit'}, MyObj{intField=94, strField='dolor sit amet'}]
    [1] received [MyObj{intField=77, strField='lorem ipsum'}]
     */
}
