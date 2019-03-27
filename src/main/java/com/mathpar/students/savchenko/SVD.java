package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.NumberR;
import com.mathpar.number.Ring;
import com.mathpar.students.savchenko.exception.WrongDimensionsException;

public class SVD {

    public static MatrixD[] getSVD(MatrixD A, Ring ring) throws WrongDimensionsException {
        // 1. QR-разложение входной матрицы A.
        MatrixD[] qr = givensQR(A, ring);
        MatrixD Q = qr[0];
        MatrixD R = qr[1];
//        System.out.println("Матрица Q = ");
//        System.out.println(Q.toString() + "\n");
//        System.out.println("Правая треугольная матрица R = ");
//        System.out.println(R.toString() + "\n");
//        System.out.println("---------- Проверка: Матрица Q*R = ");
//        System.out.println(Q.multiplyMatr(R, ring).toString() + "\n");

        // 2. Приведение матрицы R к двухдиагональному виду (D2).
        MatrixD Rt = R.transpose(ring);
        MatrixD[] lr = leftTriangleToBidiagonal(Rt, ring);
        MatrixD L1 = lr[0];
        MatrixD R1 = lr[1];
        MatrixD D2 = L1.multiplyMatr(Rt, ring);
        D2 = D2.multiplyMatr(R1, ring);
//        System.out.println("D2 = \n" + D2.toString() + "\n");

        // 3. Приведение матрицы D2 к диагональному виду (D1).
        lr = bidiagonalToDiagonal(D2, ring);
        MatrixD L2 = lr[0];
        MatrixD R2 = lr[1];
        MatrixD D1 = L2.multiplyMatr(D2, ring);
        D1 = D1.multiplyMatr(R2, ring);
        Utils.diagonalize(D1, ring);
//        System.out.println("D1 = \n" + D1.toString() + "\n");

        // 4. Расчет SVD разложения для входной матрицы A.
        MatrixD U = Q.multiplyMatr(R1, ring).multiplyMatr(R2, ring);
        MatrixD V = L2.multiplyMatr(L1, ring);
        MatrixD A1 = U.multiplyMatr(D1, ring).multiplyMatr(V, ring);
//        System.out.println("Проверка SVD разложения. U*D1*V = \n");
//        System.out.println(A1.toString());

        return new MatrixD[] {U, D1, V, A1};
    }

    public static MatrixD[] givensQR(MatrixD A, Ring ring) throws WrongDimensionsException {
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
                    GTemp = Utils.getGivensRotationMatrix(n, j-1, j, R.getElement(j-1, i), R.getElement(j, i), ring);
//                    System.out.println("МАТРИЦА ВРАЩЕНИЯ = " + "\n");
//                    System.out.println(GTemp.toString()+ "\n");
                    Q = Q.multiplyMatr(GTemp, ring);
                    R = GTemp.transpose(ring).multiplyMatr(R, ring);
//                    System.out.println("МАТРИЦА ВРАЩЕНИЯ t * Temp = " + "\n");
//                    System.out.println(R.toString() + "\n");
                }
            }
            colCounter++;
        }

        return new MatrixD[]{Q, R};
    }

    // Возвращает матрицы L, R. Матрица D2 = L*A*R имеет двухдиагональный вид.
    public static MatrixD[] leftTriangleToBidiagonal(MatrixD A, Ring ring) throws WrongDimensionsException {
        if (A.rowNum() != A.colNum())
            throw new WrongDimensionsException();

        int n = A.rowNum();
        MatrixD left;
        MatrixD right;
        MatrixD Temp = A.copy();
        MatrixD L = MatrixD.ONE(n, ring);
        MatrixD R = MatrixD.ONE(n, ring);

//        System.out.println("Обнуляем элементы в i-том столбце снизу вверх и i-той строке строке (если это не 'верхний y') \n");

        for (int col=0; col<(n-1); col++) {
            for (int row=(n-1); row>(col); row--) {
                left = Utils.getGivensRotationMatrix(n, row-1, row, Temp.getElement(row-1, col), Temp.getElement(row, col), ring);
                left = left.transpose(ring);
                L = left.multiplyMatr(L, ring);
                Temp = left.multiplyMatr(Temp, ring);
                // System.out.println("Испортился ноль в " + (row-1) + ", " + row);
                if (row > (col+1)) {                                                        // Убираем y-ки если это не "верхний" y
                    int i = row-1;
                    int j = row;
                    right = Utils.getGivensRotationMatrix(n, j-1, j, Temp.getElement(i, j-1), Temp.getElement(i, j), ring);
                    R = R.multiplyMatr(right, ring);
                    Temp = Temp.multiplyMatr(right, ring);
                }
            }
//            System.out.println("После " + (col+1) + " итерации матрица имеет вид \n");
//            System.out.println(Temp.toString() + "\n");
        }

        return new MatrixD[]{L, R};
    }

    // Возвращает матрицы L, R. Матрица D1 = L*A*R имеет диагональный вид.
    public static MatrixD[] bidiagonalToDiagonal(MatrixD A, Ring ring) throws WrongDimensionsException {
        if (A.rowNum() != A.colNum())
            throw new WrongDimensionsException();

        int n = A.rowNum();
        MatrixD left;
        MatrixD right;
        MatrixD Temp = A.copy();
        MatrixD L = MatrixD.ONE(n, ring);
        MatrixD R = MatrixD.ONE(n, ring);

//        System.out.println("Матрица имеет двухдиагональный вид. \n " +
//                "Применяем последовательное обнуление верхней/нижней диагонали, пока |элементы| > epsilon \n");
        boolean side = true;
        int iterations = 0;

        while (!Utils.checkSecondDiagonalValues(Temp, n, ring)) {
            iterations++;
            if (side) {                                                  // right
                for (int i=0; i<(n-1); i++) {
                    int j = i+1;
                    right = Utils.getGivensRotationMatrix(n, j-1, j, Temp.getElement(i, j-1), Temp.getElement(i, j), ring);
                    R = R.multiplyMatr(right, ring);
                    Temp = Temp.multiplyMatr(right, ring);
                }
            } else {                                                     // left
                for (int j=0; j<(n-1); j++) {
                    int i = j+1;
                    left = Utils.getGivensRotationMatrix(n, i-1, i, Temp.getElement(i-1, j), Temp.getElement(i, j), ring);
                    left = left.transpose(ring);
                    L = left.multiplyMatr(L, ring);
                    Temp = left.multiplyMatr(Temp, ring);
                }
            }
            side = !side;
//            System.out.println("После " + iterations + " итерации матрица имеет вид \n");
//            System.out.println(Temp.toString() + "\n");
        }

        System.out.println("Количество итераций для получения диагональной матрицы = " + iterations + ".");
        return new MatrixD[]{L, R};
    }

}
