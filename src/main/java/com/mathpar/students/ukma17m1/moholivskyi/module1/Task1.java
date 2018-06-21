package com.mathpar.students.ukma17m1.moholivskyi.module1;

import mpi.MPI;
import mpi.MPIException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by mogo on 6/21/18.
 */
public class Task1 {
    /*
    /home/mogo/tools/openmpi-3.1.0-build/bin/mpirun --oversubscribe --default-hostfile none  -np 4 java -cp /home/mogo/w/parall-progr-2017/stemedu/target/classes com/mathpar/students/ukma17m1/moholivskyi/module1/Task1
    Output:
    true
    send result
    Object successfully received
    true
    * */
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        if(MPI.COMM_WORLD.getRank() == 1)
        {
            Monkey exampleObject = new Monkey(true);
            System.out.println(exampleObject.doesLoveBanana());
            Util.sendObject(exampleObject, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            Monkey recievedObject = (Monkey) Util.recvObject(1, 1);
            System.out.println("Object successfully received");
            System.out.println(recievedObject.doesLoveBanana());
        }

        MPI.Finalize();

    }
}
