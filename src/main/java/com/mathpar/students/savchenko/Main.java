package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.NumberR;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.students.savchenko.exception.WrongDimensionsException;

public class Main {

    public static void main(String[] args) throws WrongDimensionsException {
        //runExperiment1();
        runExperiment2();
    }

    public static void runExperiment2() throws WrongDimensionsException {
        Ring ring = new Ring("R[x]");
        ring.setFLOATPOS(100);                                              // количество выводимых знаков после точки

        ring.setAccuracy(18);                                               // количество знаков после точки
        ring.setMachineEpsilonR(10);                                        // машинный ноль

        NumberR zero = ring.MachineEpsilonR;
        System.out.println("Машинный ноль = " + zero.toString(ring) + "\n");

        double st, en, lastTimeSec = 0;
        int[] dimensions = {20, 30, 40, 50, 60, 70, 80};

        MatrixD A;
        MatrixD[] svd;
        MatrixD A1;
        MatrixD difference;

        for (int i = 0; i < dimensions.length; i++) {
            A = TestData.getTestMatrix(dimensions[i], ring);
            st = System.nanoTime();
            svd = SVD.getSVD(A, ring);
            A1 = svd[3].multiplyByScalar(ring.numberMINUS_ONE, ring);
            difference = A.add(A1, ring);
            en = System.nanoTime();
            lastTimeSec = ((en-st)/1000000000);
            System.out.println("n = " + dimensions[i] + ". " +
                    "diff = " + difference.max(ring).abs(ring).toString(ring) + ". " +
                    "Time elapsed: " + lastTimeSec + " seconds.");

            System.out.println("------------------------------------------------------------------------------------");
        }

    }

    public static void runExperiment1() throws WrongDimensionsException {
        Ring ring = new Ring("R[x]");
        ring.setFLOATPOS(100);                                              // количество выводимых знаков после точки

        double st, en, lastTimeSec = 0;
        int n = 30;
        int[] accuracy = {100, 80, 60, 40, 20};

        MatrixD A = TestData.getTestMatrix(n, ring);
        MatrixD[] svd;
        MatrixD A1;
        MatrixD difference;

        for (int i = 0; i < accuracy.length; i++) {
            System.out.println("Эксперимент №" + (i+1));
            System.out.println("Accuracy = " + accuracy[i]);
            System.out.println("Machine epsilon = " + (accuracy[i] - 10));

            ring.setAccuracy(accuracy[i]);                                  // количество знаков после точки
            ring.setMachineEpsilonR(accuracy[i] - 10);                      // машинный ноль

            NumberR zero = ring.MachineEpsilonR;
            System.out.println("Машинный ноль = " + zero.toString(ring) + "\n");

            st = System.nanoTime();
            svd = SVD.getSVD(A, ring);
            A1 = svd[3].multiplyByScalar(ring.numberMINUS_ONE, ring);
            difference = A.add(A1, ring);
            en = System.nanoTime();
            lastTimeSec = ((en-st)/1000000000);
            System.out.println("n = " + n + ". " +
                    "diff = " + difference.max(ring).abs(ring).toString(ring) + ". " +
                    "Time elapsed: " + lastTimeSec + " seconds.");

            System.out.println("------------------------------------------------------------------------------------");
        }

    }

}
