package com.mathpar.students.ukma17m1.yaremko.src.lab1;

import java.awt.Color;
import java.io.IOException;

import com.mathpar.students.ukma17m1.yaremko.src.lab1.lib.MPILibrary;
import com.mathpar.students.ukma17m1.yaremko.src.lab1.model.Car;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class ObjectsMain {

    // mpirun -n 2 java -cp "/home/solomka/NetBeansProjects/stemedu/target/classes" "com/mathpar/students/ukma17m1/yaremko/src/lab1/ObjectsMain"
    /*
    Car{name=Yaguar, color=java.awt.Color[r=255,g=0,b=0]}
    Car{name=Audi, color=java.awt.Color[r=0,g=0,b=255]}
    */
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(new int[]{0, 1});
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);
        int mysize = COMM_NEW.getSize();
        int myrank = COMM_NEW.getRank();

        if (myrank == 1) {
            final Car car1 = new Car();
            car1.setColor(Color.RED);
            car1.setName("Yaguar");

            final Car car2 = new Car();
            car2.setColor(Color.BLUE);
            car2.setName("Audi");

            final Car[] cars = new Car[]{car1, car2};

            MPILibrary.sendObjects(cars, 0, 0);
        } else {
            Object[] receviedCars = MPILibrary.recvObjects(2, 1, 0);
            for (Object receivedCar : receviedCars) {
                System.out.println(receivedCar);
            }
        }

        MPI.Finalize();
    }
}
