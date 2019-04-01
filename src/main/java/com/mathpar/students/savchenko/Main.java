package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.NumberR;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.students.savchenko.exception.WrongDimensionsException;

public class Main {

    public static void main(String[] args) throws WrongDimensionsException {
        //runExperiment1();
        runExperiment2_1();
    }

    public static void runExperiment2_1() throws WrongDimensionsException {
        Ring ring = new Ring("R64[x]");
        ring.setFLOATPOS(100);                                                  // количество выводимых знаков после точки

        int[] dimensions = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        int[] machineEpsilon = {20, 40};
        MatrixD mainMatrix = TestData.getTestMatrix(dimensions[dimensions.length-1], ring);

        for (int j : machineEpsilon) {
            ring.setMachineEpsilonR64(j);                                       // машинный ноль
            NumberR64 zero = ring.MachineEpsilonR64;
            System.out.println("MachineEpsilonR64 = " + j /*zero.toString(ring)*/ + "\n");

            for (int i : dimensions) {
                MatrixD A = Utils.getSubMatrix(mainMatrix, 0, i-1, 0, i-1);
                double st = System.nanoTime();
                MatrixD[] svd = SVD.getSVD(A, ring);
                MatrixD A1 = svd[3].multiplyByScalar(ring.numberMINUS_ONE, ring);
                MatrixD difference = A.add(A1, ring);
                double en = System.nanoTime();
                double lastTimeSec = ((en - st) / 1000000000);
                System.out.println("n = " + i + ". " +
                        "diff = " + difference.max(ring).abs(ring).toString(ring) + ". " +
                        "Time elapsed: " + lastTimeSec + " seconds.");

                System.out.println("------------------------------------------------------------------------------------");
            }

            System.out.println("******************************************************************");
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
