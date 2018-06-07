package com.mathpar.students.ukma17m1.kuryliak.ch04.lab;

import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Arrays;

import static com.mathpar.students.ukma17m1.kuryliak.ch04.util.SendRecvObject.bcastObjectArray;

public class Lab05BcastArray {

    private static final int ROOT = 0;
    private static final int ARR_SIZE = 3;

    // mpirun -np 2 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab05BcastArray
    // mpirun -np 4 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab05BcastArray
    // mpirun -np 5 java -cp target/classes com.mathpar.students.ukma17m1.kuryliak.ch04.lab.Lab05BcastArray
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();

        Object[] arr = new MyObj[ARR_SIZE];
        if(rank == ROOT) {
            for(int i = 0; i < arr.length; i++) {
                arr[i] = MyObj.randomObject();
            }

            System.out.printf("[%d] broadcasting %s...\n", rank, Arrays.toString(arr));
        }

        bcastObjectArray(arr, arr.length, ROOT);
        System.out.printf("[%d] received %s\n", rank, Arrays.toString(arr));

        MPI.Finalize();
    }
    /*
    -np 2
    [0] broadcasting [MyObj{intField=16, strField='vel ad ludus mucius'}, MyObj{intField=89, strField='per ut tincidunt scriptorem'}, MyObj{intField=52, strField='per autem delenit'}]...
    [0] received [MyObj{intField=16, strField='vel ad ludus mucius'}, MyObj{intField=89, strField='per ut tincidunt scriptorem'}, MyObj{intField=52, strField='per autem delenit'}]
    [1] received [MyObj{intField=16, strField='vel ad ludus mucius'}, MyObj{intField=89, strField='per ut tincidunt scriptorem'}, MyObj{intField=52, strField='per autem delenit'}]

    -np 4
    [0] broadcasting [MyObj{intField=57, strField='nostrud'}, MyObj{intField=51, strField='dolor sit amet'}, MyObj{intField=4, strField='per ut tincidunt scriptorem'}]...
    [0] received [MyObj{intField=57, strField='nostrud'}, MyObj{intField=51, strField='dolor sit amet'}, MyObj{intField=4, strField='per ut tincidunt scriptorem'}]
    [2] received [MyObj{intField=57, strField='nostrud'}, MyObj{intField=51, strField='dolor sit amet'}, MyObj{intField=4, strField='per ut tincidunt scriptorem'}]
    [1] received [MyObj{intField=57, strField='nostrud'}, MyObj{intField=51, strField='dolor sit amet'}, MyObj{intField=4, strField='per ut tincidunt scriptorem'}]
    [3] received [MyObj{intField=57, strField='nostrud'}, MyObj{intField=51, strField='dolor sit amet'}, MyObj{intField=4, strField='per ut tincidunt scriptorem'}]

    -np 5
    [0] broadcasting [MyObj{intField=2, strField='nostrud'}, MyObj{intField=95, strField='lorem ipsum'}, MyObj{intField=94, strField='corrumpit'}]...
    [0] received [MyObj{intField=2, strField='nostrud'}, MyObj{intField=95, strField='lorem ipsum'}, MyObj{intField=94, strField='corrumpit'}]
    [3] received [MyObj{intField=2, strField='nostrud'}, MyObj{intField=95, strField='lorem ipsum'}, MyObj{intField=94, strField='corrumpit'}]
    [4] received [MyObj{intField=2, strField='nostrud'}, MyObj{intField=95, strField='lorem ipsum'}, MyObj{intField=94, strField='corrumpit'}]
    [1] received [MyObj{intField=2, strField='nostrud'}, MyObj{intField=95, strField='lorem ipsum'}, MyObj{intField=94, strField='corrumpit'}]
    [2] received [MyObj{intField=2, strField='nostrud'}, MyObj{intField=95, strField='lorem ipsum'}, MyObj{intField=94, strField='corrumpit'}]
     */
}
