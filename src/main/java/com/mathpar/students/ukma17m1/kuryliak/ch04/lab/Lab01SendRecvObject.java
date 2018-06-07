package com.mathpar.students.ukma17m1.kuryliak.ch04.lab;

import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;

import static com.mathpar.students.ukma17m1.kuryliak.ch04.util.SendRecvObject.recvObject;
import static com.mathpar.students.ukma17m1.kuryliak.ch04.util.SendRecvObject.sendObject;

public class Lab01SendRecvObject {

    // mpirun -np 2 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab01SendRecvObject
    // mpirun -np 4 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab01SendRecvObject
    // mpirun -np 5 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab01SendRecvObject
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        if(rank % 2 == 0) {
            int recipient = rank+1;

            if (recipient >= size) {
                System.out.printf("[%d] not sending anything :(\n", rank);
            } else {
                MyObj obj = MyObj.randomObject();
                System.out.printf("[%d] sending %s...\n", rank, obj);
                sendObject(obj, recipient, 0);
            }
        } else {
            MyObj obj = (MyObj) recvObject(rank-1, 0);
            System.out.printf("[%d] received %s\n", rank, obj);
        }

        MPI.Finalize();
    }
    /*
    -np 2
    [0] sending MyObj{intField=46, strField='nostrud'}...
    [1] received MyObj{intField=46, strField='nostrud'}

    -np 4
    [0] sending MyObj{intField=46, strField='nostrud'}...
    [2] sending MyObj{intField=41, strField='deleniti ut sit'}...
    [1] received MyObj{intField=46, strField='nostrud'}
    [3] received MyObj{intField=41, strField='deleniti ut sit'}

    -np 5
    [0] sending MyObj{intField=16, strField='dolor sit amet'}...
    [4] not sending anything :(
    [2] sending MyObj{intField=86, strField='platonem no etiam'}...
    [3] received MyObj{intField=86, strField='platonem no etiam'}
    [1] received MyObj{intField=16, strField='dolor sit amet'}
     */
}
