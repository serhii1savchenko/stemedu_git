package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.Ring;

import java.util.Random;

public class TestData {

    public static MatrixD getTestMatrix(int n, Ring ring) {
        Random random = new Random(1);

        int[][] matrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }

        MatrixD matrixD = new MatrixD(matrix, ring);

        return matrixD;
    }

}
