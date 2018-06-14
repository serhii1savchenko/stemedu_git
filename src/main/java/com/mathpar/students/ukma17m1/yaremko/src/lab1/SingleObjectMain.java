package com.mathpar.students.ukma17m1.yaremko.src.lab1;

import java.awt.Color;
import java.io.IOException;

import com.mathpar.students.ukma17m1.yaremko.src.lab1.lib.MPILibrary;
import com.mathpar.students.ukma17m1.yaremko.src.lab1.model.Car;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class SingleObjectMain {

    // mpirun -n 2 java -cp "/home/solomka/NetBeansProjects/stemedu/target/classes" "com/mathpar/students/ukma17m1/yaremko/src/lab1/SingleObjectMain"
    // Car{name=Yaguar, color=java.awt.Color[r=255,g=0,b=0]}
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        final Car car = new Car();
        car.setColor(Color.RED);
        car.setName("Yaguar");
        
        MPI.Init(args);
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(new int[]{0, 1});
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);
        int mysize = COMM_NEW.getSize();
        int myrank = COMM_NEW.getRank();

        if (myrank == 1) {
            MPILibrary.sendObject(car, 0, 0);
        } else {
            System.out.println(MPILibrary.recvObject(1, 0));
        }

        MPI.Finalize();
    }
}
