package com.mathpar.students.ukma17m1.kuryliak.ch04.lab;

import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Arrays;

import static com.mathpar.students.ukma17m1.kuryliak.ch04.util.SendRecvObject.recvArrayOfObjects;
import static com.mathpar.students.ukma17m1.kuryliak.ch04.util.SendRecvObject.sendArrayOfObjects;

public class Lab02SendRecvArray {

    // mpirun -np 2 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab02SendRecvArray
    // mpirun -np 4 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab02SendRecvArray
    // mpirun -np 5 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab02SendRecvArray
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
                sendArrayOfObjects(arr, recipient, 0);
            }
        } else {
            int arrLen = rank;
            int sender = rank - 1;

            Object[] arr = recvArrayOfObjects(arrLen, sender, 0);
            System.out.printf("[%d] received %s\n", rank, Arrays.toString(arr));
        }

        MPI.Finalize();
    }
    /*
    -np 2
    [0] sending [MyObj{intField=71, strField='nostrud'}]...
    [1] received [MyObj{intField=71, strField='nostrud'}]

    -np 4
    [0] sending [MyObj{intField=93, strField='lorem ipsum'}]...
    [2] sending [MyObj{intField=40, strField='vis harum principes te in vero'}, MyObj{intField=7, strField='dolor sit amet'}, MyObj{intField=87, strField='lorem ipsum'}]...
    [1] received [MyObj{intField=93, strField='lorem ipsum'}]
    [3] received [MyObj{intField=40, strField='vis harum principes te in vero'}, MyObj{intField=7, strField='dolor sit amet'}, MyObj{intField=87, strField='lorem ipsum'}]

    -np 5
    [2] sending [MyObj{intField=73, strField='vel ad ludus mucius'}, MyObj{intField=92, strField='nostrud'}, MyObj{intField=16, strField='dolor sit amet'}]...
    [0] sending [MyObj{intField=14, strField='nostrud'}]...
    [4] not sending anything :(
    [1] received [MyObj{intField=14, strField='nostrud'}]
    [3] received [MyObj{intField=73, strField='vel ad ludus mucius'}, MyObj{intField=92, strField='nostrud'}, MyObj{intField=16, strField='dolor sit amet'}]
     */
}
