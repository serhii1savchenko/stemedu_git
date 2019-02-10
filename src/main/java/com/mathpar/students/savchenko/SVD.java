package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.students.savchenko.exception.WrongDimensionsException;

public class SVD {

    public static void main(String[] args) {
        Ring ring = new Ring("R64[x]");

        ring.setMachineEpsilonR64(9);                                                          // установка машинного нуля
        ring.FLOATPOS = 15;                                                                    // количество знаков после точки
        NumberR64 zero = ring.MachineEpsilonR64;
        System.out.println("Машинный ноль = " + zero.toString(ring) + "\n");

        MatrixD A = new MatrixD(5, 5, 20, ring);                                    // n x n матрица случайных чисел
        System.out.println("Входная матрица A = ");
        System.out.println(A.toString() + "\n");

        try {
            // 1. QR-разложение входной матрицы A.
            MatrixD[] qr = givensQR(A, ring);
            MatrixD Q = qr[0];
            MatrixD R = qr[1];
            System.out.println("Матрица Q = ");
            System.out.println(Q.toString() + "\n");
            System.out.println("Правая треугольная матрица R = ");
            System.out.println(R.toString() + "\n");
            System.out.println("---------- Проверка: Матрица Q*R = ");
            System.out.println(Q.multCU(R, ring).toString() + "\n");

            // 2. Приведение матрицы R к двухдиагональному виду (D2).
            MatrixD Rt = R.transpose(ring);
            MatrixD[] lr = leftTriangleToBidiagonal(Rt, ring);
            MatrixD L1 = lr[0];
            MatrixD R1 = lr[1];
            MatrixD D2 = L1.multCU(Rt, ring);
            D2 = D2.multCU(R1, ring);
            System.out.println("D2 = \n" + D2.toString() + "\n");

            // 3. Приведение матрицы D2 к диагональному виду (D1).
            lr = bidiagonalToDiagonal(D2, ring);
            MatrixD L2 = lr[0];
            MatrixD R2 = lr[1];
            MatrixD D1 = L2.multCU(D2, ring);
            D1 = D1.multCU(R2, ring);
            System.out.println("D1 = \n" + D1.toString() + "\n");

            // 4. Расчет SVD разложения для входной матрицы A.
            MatrixD U = Q.multCU(R1, ring).multCU(R2, ring);
            MatrixD V = L2.multCU(L1, ring);
            MatrixD A1 = U.multCU(D1, ring).multCU(V, ring);
            System.out.println("Проверка SVD разложения. U*D1*V = \n");
            System.out.println(A1.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    Q = Q.multCU(GTemp, ring);
                    R = GTemp.transpose(ring).multCU(R, ring);
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
                L = left.multCU(L, ring);
                Temp = left.multCU(Temp, ring);
                // System.out.println("Испортился ноль в " + (row-1) + ", " + row);
                if (row > (col+1)) {                                                        // Убираем y-ки если это не "верхний" y
                    int i = row-1;
                    int j = row;
                    right = Utils.getGivensRotationMatrix(n, j-1, j, Temp.getElement(i, j-1), Temp.getElement(i, j), ring);
                    R = R.multCU(right, ring);
                    Temp = Temp.multCU(right, ring);
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
                    R = R.multCU(right, ring);
                    Temp = Temp.multCU(right, ring);
                }
            } else {                                                     // left
                for (int j=0; j<(n-1); j++) {
                    int i = j+1;
                    left = Utils.getGivensRotationMatrix(n, i-1, i, Temp.getElement(i-1, j), Temp.getElement(i, j), ring);
                    left = left.transpose(ring);
                    L = left.multCU(L, ring);
                    Temp = left.multCU(Temp, ring);
                }
            }
            side = !side;
//            System.out.println("После " + iterations + " итерации матрица имеет вид \n");
//            System.out.println(Temp.toString() + "\n");
        }

        System.out.println("Количество итераций для получения диагональной матрицы = " + iterations + ". \n");
        return new MatrixD[]{L, R};
    }

}
