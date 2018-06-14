package com.mathpar.students.ukma17m1.yaremko.src.lab1;

import java.awt.Color;
import java.io.IOException;

import com.mathpar.students.ukma17m1.yaremko.src.lab1.lib.MPILibrary;
import com.mathpar.students.ukma17m1.yaremko.src.lab1.model.Car;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class BCastObjectMain {

    // mpirun -n 4 java -cp "/home/solomka/NetBeansProjects/stemedu/target/classes" "com/mathpar/students/ukma17m1/yaremko/src/lab1/BCastObjectMain"
    /*
    myrank=1 Car{name=Yaguar, color=java.awt.Color[r=255,g=0,b=0]}
    myrank=2 Car{name=Yaguar, color=java.awt.Color[r=255,g=0,b=0]}
    myrank=3 Car{name=Yaguar, color=java.awt.Color[r=255,g=0,b=0]}
     */
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {

        MPI.Init(args);
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(new int[]{0, 1, 2, 3});
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);
        int myrank = COMM_NEW.getRank();

        if (myrank == 0) {
            final Car car = new Car();
            car.setColor(Color.RED);
            car.setName("Yaguar");
            MPILibrary.bcastObject(car, 0);
        } else {
            System.out.println("myrank=" + myrank + " " + MPILibrary.bcastObject(new Car(), 0));
        }

        MPI.Finalize();
    }
}
