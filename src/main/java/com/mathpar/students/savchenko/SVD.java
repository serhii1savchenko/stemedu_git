package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.NumberR;
import com.mathpar.number.Ring;
import com.mathpar.students.savchenko.exception.WrongDimensionsException;

public class SVD {

    public static void main(String[] args) {
        Ring ring = new Ring("R[x]");
        MatrixD A = new MatrixD(5, 5, 100, ring);               // n x n матрица случайных чисел

        System.out.println("Входная матрица A = ");
        System.out.println(A.toString() + "\n");

        try {
            givensQR(A, ring);
        } catch (WrongDimensionsException e) {
            e.printStackTrace();
        }
    }

    public static void givensQR(MatrixD A, Ring ring) throws WrongDimensionsException {
        if (A.rowNum() != A.colNum())
            throw new WrongDimensionsException();

        int colCounter = 1;
        int n = A.rowNum();
        MatrixD Q = MatrixD.ONE(n, ring);
        MatrixD R = A.copy();
        MatrixD GTemp;

        for (int i=0; i<n-1; i++) {
//            System.out.println("ИТЕРАЦИЯ " + i  + "\n");
            for (int j=n-1; j>colCounter-1; j--) {
                if (Math.abs(R.getElement(j, i).doubleValue()) > 0) {
//                    System.out.println("ОБНУЛЯЕМ ЭЛЕМЕНТ " + j + ", " + i + "\n");
                    GTemp = getGivensRotationMatrix(n, j-1, j, R.getElement(j-1, i).doubleValue(), R.getElement(j, i).doubleValue(), ring);
//                    System.out.println("МАТРИЦА ВРАЩЕНИЯ = " + "\n");
//                    System.out.println(GTemp.toString()+ "\n");
                    Q = Q.multCU(GTemp, ring);
                    R = GTemp.transpose(ring).multCU(R, ring);
//                    System.out.println("МАТРИЦА ВРАЩЕНИЯ t * Temp = " + "\n");
//                    System.out.println(R.toString() + "\n");
                }
            }
            colCounter++;
        }

        System.out.println("Матрица Q = ");
        System.out.println(Q.toString() + "\n");

        System.out.println("Правая треугольная матрица R = ");
        System.out.println(R.toString() + "\n");

        System.out.println("---------- Проверка: Матрица Q*R = ");
        System.out.println(Q.multCU(R, ring).toString());

    }

    public static MatrixD getGivensRotationMatrix(int n, int i, int j, double a, double b, Ring ring) {
        MatrixD G = MatrixD.ONE(n, ring);
        double r = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        double c = a/r;
        double s = (-b)/r;
        G.M[i][i] = new NumberR(c);
        G.M[i][j] = new NumberR(s);
        G.M[j][i] = new NumberR(-s);
        G.M[j][j] = new NumberR(c);
        return G;
    }

}
