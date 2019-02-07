package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.students.savchenko.exception.WrongDimensionsException;

public class SVD {

    /*
        1. QR разложение - givensQR()
        2. Получить матрицу вращения Гивенса - getGivensRotationMatrix()
        3. Получить левую и правую матрицы вращения для обнуления двух внедиагональных элементов - getTwoGivensRotationMatrices()
     */

    public static void main(String[] args) {
        Ring ring = new Ring("R64[x]");
        ring.FLOATPOS = 15;
        NumberR64 zero = ring.MachineEpsilonR64;
        System.out.println(zero.toString(ring) + "\n");

        try {
            MatrixD A = new MatrixD(5, 5, 10, ring);               // n x n матрица случайных чисел
            System.out.println("Входная матрица A = ");
            System.out.println(A.toString() + "\n");
            MatrixD[] qr = givensQR(A, ring);
            System.out.println("Матрица Q = ");
            System.out.println(qr[0].toString() + "\n");
            System.out.println("Правая треугольная матрица R = ");
            System.out.println(qr[1].toString() + "\n");
            System.out.println("---------- Проверка: Матрица Q*R = ");
            System.out.println(qr[0].multCU(qr[1], ring).toString());

//            long [][] arr = {{1, 0}, {5, 2}};
//            MatrixD test = new MatrixD(arr, ring);
//            MatrixD[] lr = getTwoGivensRotationMatrices(test.getElement(0,0).doubleValue(), test.getElement(1, 0).doubleValue(),
//                    test.getElement(1,1).doubleValue(), 2, 0, 1, ring);
//            System.out.println("L * A * R = \n");
//            System.out.println(lr[0].multCU(test, ring).multCU(lr[1], ring).toString());
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
                    GTemp = getGivensRotationMatrix(n, j-1, j, R.getElement(j-1, i), R.getElement(j, i), ring);
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

    public static MatrixD getGivensRotationMatrix(int n, int i, int j, Element a, Element b, Ring ring) {
        MatrixD G = MatrixD.ONE(n, ring);
        Element r = a.pow(2, ring).add(b.pow(2, ring), ring).sqrt(ring);     // Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        Element c = a.divide(r, ring);                                            // a/r;
        Element s = b.negate(ring).divide(r, ring);                               // (-b)/r;
        G.M[i][i] = c;
        G.M[i][j] = s;
        G.M[j][i] = s.negate(ring);
        G.M[j][j] = c;
        return G;
    }

    public static MatrixD[] getTwoGivensRotationMatrices(double a, double b, double d, int n, int i, int j, Ring ring) {
        MatrixD left = MatrixD.ONE(n, ring);
        MatrixD right = MatrixD.ONE(n, ring);
        double c = 0d;
        double s = 0d;
        double C = 0d;
        double S = 0d;
        double t = 0d;
        double T = 0d;

        if (a != 0d && d != 0d) {
//            System.out.println("a ≠ 0, d ≠ 0 \n");
            t = ((-1d*(b*b+d*d-a*a)) + Math.sqrt(Math.pow((b*b+d*d-a*a), 2) + 4d*a*a*b*b)) / (2d*a*b);
            T = (-1d/d)*(a*t + b);
        } else if (a != 0d && d == 0d) {
//            System.out.println("a ≠ 0, d = 0 \n");
            T = 0d;
            t = (-b)/a;
        } else if (a == 0d && d != 0d) {
//            System.out.println("a = 0, d ≠ 0 \n");
            t = 0d;
            T = (-b)/d;
        } else {                                    // a = 0 & d = 0
//            System.out.println("a = 0, d = 0 \n");
            c = 1d;
            s = 0d;
            C = 0d;
            S = 1d;
        }

        if (!(a == 0d && d == 0d)) {
            c = Math.sqrt(1d/(1d+t*t));                                 // c = Math.cos(Math.atan(t));
            s = t*c;                                                    // s = Math.sin(Math.atan(t));
            C = Math.sqrt(1d/(1d+T*T));                                 // C = Math.cos(Math.atan(T));
            S = T*C;                                                    // S = Math.sin(Math.atan(T));
        }

        left.M[i][i] = new NumberR64(c);
        left.M[i][j] = new NumberR64(-s);
        left.M[j][i] = new NumberR64(s);
        left.M[j][j] = new NumberR64(c);

        right.M[i][i] = new NumberR64(C);
        right.M[i][j] = new NumberR64(-S);
        right.M[j][i] = new NumberR64(S);
        right.M[j][j] = new NumberR64(C);

        MatrixD[] result = {left, right};
        return result;
    }

}
