package com.mathpar.students.ukma17m1.moholivskyi.module1;

import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;


/**
 * Created by mogo on 6/21/18.
 */
public class Task3 {
    /*
    /home/mogo/tools/openmpi-3.1.0-build/bin/mpirun --oversubscribe --default-hostfile none  -np 4 java -cp /home/mogo/w/parall-progr-2017/stemedu/target/classes com/mathpar/students/ukma17m1/moholivskyi/module1/Task3
    Output:
    Objects successfully received
    false
    true
    false
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

        if(MPI.COMM_WORLD.getRank() == 1)
        {
            Util.sendObjects(exampleObjectsArray, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            Monkey[] recievedExampleObjects = Util.convertObjects(Util.recvObjects(4,1,1));
            System.out.println("Objects successfully received");

            for (Monkey exampleObject:recievedExampleObjects) {
                System.out.println(exampleObject.doesLoveBanana());
            }
        }

        MPI.Finalize();

    }
}
