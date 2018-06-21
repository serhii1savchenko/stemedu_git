package com.mathpar.students.ukma17m1.moholivskyi.module1;

import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;


/**
 * Created by mogo on 6/21/18.
 */
public class Task5 {
    /*
    /home/mogo/tools/openmpi-3.1.0-build/bin/mpirun --oversubscribe --default-hostfile none  -np 4 java -cp /home/mogo/w/parall-progr-2017/stemedu/target/classes com/mathpar/students/ukma17m1/moholivskyi/module1/Task5
    Output:
    Starting programm
    Success. Rank is 0
    Success. Rank is 3
    Success. Rank is 2
    Success. Rank is 1
    * */
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        Monkey var1 = new Monkey(false);
        Monkey var2 = new Monkey(true);
        Monkey var3 = new Monkey(false);
        Monkey var4 = new Monkey(true);
        Monkey var5 = new Monkey(true);
        Monkey[] exampleObjectsArray = {var1,var2,var3,var4, var5};

        System.out.println("Starting programm");
        Util.bcastObjectArray(exampleObjectsArray, exampleObjectsArray.length, MPI.COMM_WORLD.getRank());

        System.out.println("Success. Rank is " + MPI.COMM_WORLD.getRank());


        MPI.Finalize();

    }
}
