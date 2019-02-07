package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.students.savchenko.exception.WrongDimensionsException;

public class LeftTriangleMatrixSVD {

    /*
        1. приведение нижней треугольной матрицы к диагональному виду - leftTriangleSVD()
     */

    public static void main(String[] args) {
        Ring ring = new Ring("R64[x]");
        ring.FLOATPOS = 15;
        NumberR64 zero = ring.MachineEpsilonR64;

        MatrixD L = getTriangleMatrixNumber64(4, 20, ring);
        System.out.println("Нижняя треугольная матрица L = " + "\n");
        System.out.println(L.toString() + "\n");

        try {
            leftTriangleSVD(L, ring);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void leftTriangleSVD(MatrixD L, Ring ring) throws WrongDimensionsException {
        if (L.rowNum() != L.colNum())
            throw new WrongDimensionsException();

        int n = L.rowNum();
        MatrixD left;
        MatrixD right;
        MatrixD Temp = L.copy();

        System.out.println("Обнуляем элементы в i-том столбце снизу вверх и i-той строке строке (если это не 'верхний y') \n");

        for (int col=0; col<(n-1); col++) {
            for (int row=(n-1); row>(col); row--) {
                left = SVD.getGivensRotationMatrix(n, row-1, row, Temp.getElement(row-1, col), Temp.getElement(row, col), ring);
                left = left.transpose(ring);
                Temp = left.multCU(Temp, ring);
                // System.out.println("Испортился ноль в " + (row-1) + ", " + row);
                if (row > (col+1)) {                                                        // Убираем y-ки если это не "верхний" y
                    int i = row-1;
                    int j = row;
                    right = SVD.getGivensRotationMatrix(n, j-1, j, Temp.getElement(i, j-1), Temp.getElement(i, j), ring);
                    Temp = Temp.multCU(right, ring);
                }
            }
            System.out.println("После " + (col+1) + " итерации матрица имеет вид \n");
            System.out.println(Temp.toString() + "\n");
        }

        System.out.println("Матрица имеет двухдиагональный вид. \n " +
                "Применяем последовательное обнуление верхней/нижней диагонали, пока |элементы| > epsilon \n");

        boolean side = true;
        int iterations = 0;

        while (!checkSecondDiagonalValues(Temp, n, ring)) {
            iterations++;
            if (side) {                                                  // right
                for (int i=0; i<(n-1); i++) {
                    int j = i+1;
                    right = SVD.getGivensRotationMatrix(n, j-1, j, Temp.getElement(i, j-1), Temp.getElement(i, j), ring);
                    Temp = Temp.multCU(right, ring);
                }
            } else {                                                     // left
                for (int j=0; j<(n-1); j++) {
                    int i = j+1;
                    left = SVD.getGivensRotationMatrix(n, i-1, i, Temp.getElement(i-1, j), Temp.getElement(i, j), ring);
                    left = left.transpose(ring);
                    Temp = left.multCU(Temp, ring);
                }
            }
            side = !side;
            System.out.println("После " + iterations + " итерации матрица имеет вид \n");
            System.out.println(Temp.toString() + "\n");
        }

        System.out.println("КОЛИЧЕСТВО ИТЕРАЦИЙ = " + iterations);
    }

    private static boolean checkSecondDiagonalValues(MatrixD temp, int n, Ring ring) {
        for (int i = 0; i < (n-1); i++) {
            if (!temp.getElement(i, i+1).abs(ring).isZero(ring) || !temp.getElement(i+1, i).abs(ring).isZero(ring))
                return false;
        }
        return true;
    }


    public static MatrixD getTriangleMatrixNumber64(int n, int mod, Ring ring) {
        MatrixD L = new MatrixD(n, n, mod, ring);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (!(j<=i))
                    L.M[i][j] = new NumberR64(0d);
        return L;
    }

}
