package com.mathpar.students.ukma17m1.moholivskyi.module1;

import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;


/**
 * Created by mogo on 6/21/18.
 */
public class Task4 {
    /*
    /home/mogo/tools/openmpi-3.1.0-build/bin/mpirun --oversubscribe --default-hostfile none  -np 4 java -cp /home/mogo/w/parall-progr-2017/stemedu/target/classes com/mathpar/students/ukma17m1/moholivskyi/module1/Task4
    Output:
    true
        2 processor sends
        true
        3 processor sends
        true
        1 processor sends
        true
        0 processor sends
        3 got exampleObject
        true
        0 got exampleObject2 got exampleObject
        true
        true
        1 got exampleObject
        true
    * */
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        Monkey var1 = new Monkey(false);
        Monkey var2 = new Monkey(true);
        Monkey var3 = new Monkey(false);
        Monkey var4 = new Monkey(true);
        Monkey var5 = new Monkey(true);
        Monkey[] exampleObjectsArray = {var1,var2,var3,var4, var5};

        Monkey exampleObject = new Monkey(true);
        System.out.println(exampleObject.doesLoveBanana());
        System.out.println(MPI.COMM_WORLD.getRank() + " processor sends");

        Monkey receivedMonkey = (Monkey) Util.bcastObject(exampleObject, MPI.COMM_WORLD.getRank());
        System.out.println(MPI.COMM_WORLD.getRank() +" got exampleObject");
        System.out.println(receivedMonkey.doesLoveBanana());

        MPI.Finalize();

    }
}
