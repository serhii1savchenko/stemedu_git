package com.mathpar.students.ukma17m1.kuryliak.ch04.lab;

import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;

import static com.mathpar.students.ukma17m1.kuryliak.ch04.util.SendRecvObject.bcastObject;

public class Lab04BcastObject {

    private static final int ROOT = 0;

    // mpirun -np 2 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab04BcastObject
    // mpirun -np 4 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab04BcastObject
    // mpirun -np 5 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab04BcastObject
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();

        MyObj obj = null;
        if(rank == ROOT) {
            obj = MyObj.randomObject();
            System.out.printf("[%d] broadcasting %s...\n", rank, obj);
        }

        obj = (MyObj) bcastObject(obj, ROOT);
        System.out.printf("[%d] received %s\n", rank, obj);

        MPI.Finalize();
    }
    /*
    -np 2
    [0] broadcasting MyObj{intField=8, strField='lorem ipsum'}...
    [0] received MyObj{intField=8, strField='lorem ipsum'}
    [1] received MyObj{intField=8, strField='lorem ipsum'}

    -np 4
    [0] broadcasting MyObj{intField=79, strField='dolor sit amet'}...
    [0] received MyObj{intField=79, strField='dolor sit amet'}
    [1] received MyObj{intField=79, strField='dolor sit amet'}
    [3] received MyObj{intField=79, strField='dolor sit amet'}
    [2] received MyObj{intField=79, strField='dolor sit amet'}

    -np 5
    [0] broadcasting MyObj{intField=17, strField='platonem no etiam'}...
    [0] received MyObj{intField=17, strField='platonem no etiam'}
    [3] received MyObj{intField=17, strField='platonem no etiam'}
    [2] received MyObj{intField=17, strField='platonem no etiam'}
    [1] received MyObj{intField=17, strField='platonem no etiam'}
    [4] received MyObj{intField=17, strField='platonem no etiam'}
     */
}
