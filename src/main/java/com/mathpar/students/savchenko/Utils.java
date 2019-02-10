package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;

public class Utils {

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

    public static boolean checkSecondDiagonalValues(MatrixD temp, int n, Ring ring) {
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

        return new MatrixD[]{left, right};
//            long [][] arr = {{1, 0}, {5, 2}};
//            MatrixD test = new MatrixD(arr, ring);
//            MatrixD[] lr = getTwoGivensRotationMatrices(test.getElement(0,0).doubleValue(), test.getElement(1, 0).doubleValue(),
//                    test.getElement(1,1).doubleValue(), 2, 0, 1, ring);
//            System.out.println("L * A * R = \n");
//            System.out.println(lr[0].multCU(test, ring).multCU(lr[1], ring).toString());
    }

}
