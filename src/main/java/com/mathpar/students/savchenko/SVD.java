package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.students.savchenko.exception.WrongDimensionsException;

public class SVD {

    public static void main(String[] args) {
        Ring ring = new Ring("R64[x]");
        MatrixD A = new MatrixD(4, 4, 10, ring);               // n x n матрица случайных чисел

        System.out.println("Входная матрица A = ");
        System.out.println(A.toString() + "\n");

        try {
//            givensQR(A, ring);
            getTwoDiagonalMatrixByGivensRotation(A, ring);
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

    public static void getTwoDiagonalMatrixByGivensRotation(MatrixD A, Ring ring) throws WrongDimensionsException {
        if (A.rowNum() != A.colNum())
            throw new WrongDimensionsException();

        int colCounter = 1;
        int rowCounter = 2;
        int n = A.rowNum();
        MatrixD U = MatrixD.ONE(n, ring);
        MatrixD V = MatrixD.ONE(n, ring);
        MatrixD Temp = A.copy();
        MatrixD UTemp, VTemp;

        for (int i=0; i<n-1; i++) {
            System.out.println("ИТЕРАЦИЯ " + i  + "\n");
            for (int j=n-1; j>colCounter-1; j--) {
                if (Math.abs(Temp.getElement(j, i).doubleValue()) > 0) {
                    System.out.println("ОБНУЛЯЕМ ЭЛЕМЕНТ в столбце " + j + ", " + i + "\n");
                    UTemp = getGivensRotationMatrix(n, j-1, j, Temp.getElement(j-1, i).doubleValue(), Temp.getElement(j, i).doubleValue(), ring);
//                    System.out.println("МАТРИЦА ВРАЩЕНИЯ = " + "\n");
//                    System.out.println(UTemp.toString()+ "\n");
                    U = UTemp.multCU(U, ring);
                    Temp = UTemp.transpose(ring).multCU(Temp, ring);
//                    System.out.println("МАТРИЦА ВРАЩЕНИЯ t * Temp = " + "\n");
//                    System.out.println(Temp.toString() + "\n");
                }
            }
            colCounter++;
            // TODO написать метод умножения матрицы на транспонированую матрицу, чтобы избежать явного создания Temp.transpose(ring);
            Temp = Temp.transpose(ring);
            for (int j=n-1; j>rowCounter-1; j--) {
                if (Math.abs(Temp.getElement(j, i).doubleValue()) > 0) {
                    System.out.println("ОБНУЛЯЕМ ЭЛЕМЕНТ в строке " + i + ", " + j + "\n");
                    // TODO
                    VTemp = getGivensRotationMatrix(n, j-1, j, Temp.getElement(j-1, i).doubleValue(), Temp.getElement(j, i).doubleValue(), ring);
//                    System.out.println("МАТРИЦА ВРАЩЕНИЯ = " + "\n");
//                    System.out.println(VTemp.toString()+ "\n");
                    V = V.multCU(VTemp, ring);
                    Temp = VTemp.transpose(ring).multCU(Temp, ring);
//                    System.out.println("МАТРИЦА ВРАЩЕНИЯ t * Temp t = " + "\n");
//                    System.out.println(Temp.toString() + "\n");
                }
            }
            // TODO
            Temp = Temp.transpose(ring);
            rowCounter++;
            System.out.println("Temp = ");
            System.out.println(Temp.toString() + "\n");
        }
        System.out.println("======= Результат =======" + "\n");
        System.out.println("U = ");
        System.out.println(U.toString()  + "\n");
        System.out.println("V = ");
        System.out.println(V.toString() + "\n");
        System.out.println("Result = ");
        System.out.println(Temp.toString());
    }

    public static MatrixD getGivensRotationMatrix(int n, int i, int j, double a, double b, Ring ring) {
        MatrixD G = MatrixD.ONE(n, ring);
        double r = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        double c = a/r;
        double s = (-b)/r;
        G.M[i][i] = new NumberR64(c);
        G.M[i][j] = new NumberR64(s);
        G.M[j][i] = new NumberR64(-s);
        G.M[j][j] = new NumberR64(c);
        return G;
    }

}
