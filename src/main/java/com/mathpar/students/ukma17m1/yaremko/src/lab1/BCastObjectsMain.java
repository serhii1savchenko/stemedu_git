package com.mathpar.students.ukma17m1.yaremko.src.lab1;

import java.awt.Color;
import java.io.IOException;

import com.mathpar.students.ukma17m1.yaremko.src.lab1.lib.MPILibrary;
import com.mathpar.students.ukma17m1.yaremko.src.lab1.model.Car;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

public class BCastObjectsMain {

    // mpirun -n 4 java -cp "/home/solomka/NetBeansProjects/stemedu/target/classes" "com/mathpar/students/ukma17m1/yaremko/src/lab1/BCastObjectsMain"
    /*
    myrank=1 Car{name=Yaguar, color=java.awt.Color[r=255,g=0,b=0]}
    myrank=1 Car{name=Audi, color=java.awt.Color[r=0,g=0,b=255]}
    myrank=3 Car{name=Yaguar, color=java.awt.Color[r=255,g=0,b=0]}
    myrank=3 Car{name=Audi, color=java.awt.Color[r=0,g=0,b=255]}
    myrank=2 Car{name=Yaguar, color=java.awt.Color[r=255,g=0,b=0]}
    myrank=2 Car{name=Audi, color=java.awt.Color[r=0,g=0,b=255]}

     */
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(new int[]{0, 1, 2, 3});
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);
        int mysize = COMM_NEW.getSize();
        int myrank = COMM_NEW.getRank();

        if (myrank == 0) {
            final Car car1 = new Car();
            car1.setColor(Color.RED);
            car1.setName("Yaguar");

            final Car car2 = new Car();
            car2.setColor(Color.BLUE);
            car2.setName("Audi");

            final Car[] cars = new Car[]{car1, car2};

            MPILibrary.bcastObjectArray(cars, cars.length, 0);
        } else {
            Car[] cars = new Car[2];
            MPILibrary.bcastObjectArray(cars, cars.length, 0);
            for (Car car : cars) {
                System.out.println("myrank=" + myrank + " " + car);
            }
        }

        MPI.Finalize();
    }
}
