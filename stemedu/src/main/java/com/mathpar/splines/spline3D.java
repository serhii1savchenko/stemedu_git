package com.mathpar.splines;

import com.mathpar.polynom.Polynom;

/**
 *
 * @author yuri
 */
public class spline3D {

    private int n;
    private int m;
    private int l;
    private polynom_triCubic[][][] polynoms;

    public spline3D(double[][][] f) {
        buildTriCubic(f);
    }

    private void buildTriCubic(double[][][] f) {
        n = f.length;
        m = f[0].length;
        l = f[0][0].length;
        System.out.println("3d build derivatives start");
        derivative3D d = new derivative3D(f);
        System.out.println("3d build derivatives finish");

        polynoms = new polynom_triCubic[n - 1][m - 1][l - 1];
        double[][] matrix = new double[][]{// Это не картинка в формате jpeg, а матрица для 64 уравнений :) для получения полинома третьей степени от трёх полиномов. Была сгенерирована с помощью класса matrixGenerator.
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {-3, 3, 0, 0, 0, 0, 0, 0, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, -2, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, 3, 0, 0, 0, 0, 0, 0, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, -2, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {-3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, -3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {9, -9, 9, -9, 0, 0, 0, 0, 6, 3, -3, -6, 0, 0, 0, 0, 6, -6, -3, 3, 0, 0, 0, 0, 4, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {-6, 6, -6, 6, 0, 0, 0, 0, -3, -3, 3, 3, 0, 0, 0, 0, -4, 4, 2, -2, 0, 0, 0, 0, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {-6, 6, -6, 6, 0, 0, 0, 0, -4, -2, 2, 4, 0, 0, 0, 0, -3, 3, 3, -3, 0, 0, 0, 0, -2, -1, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {4, -4, 4, -4, 0, 0, 0, 0, 2, 2, -2, -2, 0, 0, 0, 0, 2, -2, -2, 2, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, 3, 0, 0, 0, 0, 0, 0, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, -2, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, 3, 0, 0, 0, 0, 0, 0, -2, -1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, -2, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, -1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, -9, 9, -9, 0, 0, 0, 0, 6, 3, -3, -6, 0, 0, 0, 0, 6, -6, -3, 3, 0, 0, 0, 0, 4, 2, 1, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -6, 6, -6, 6, 0, 0, 0, 0, -3, -3, 3, 3, 0, 0, 0, 0, -4, 4, 2, -2, 0, 0, 0, 0, -2, -2, -1, -1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -6, 6, -6, 6, 0, 0, 0, 0, -4, -2, 2, 4, 0, 0, 0, 0, -3, 3, 3, -3, 0, 0, 0, 0, -2, -1, -1, -2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, -4, 4, -4, 0, 0, 0, 0, 2, 2, -2, -2, 0, 0, 0, 0, 2, -2, -2, 2, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
            {-3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, -3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {9, -9, 0, 0, -9, 9, 0, 0, 6, 3, 0, 0, -6, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, -6, 0, 0, 3, -3, 0, 0, 4, 2, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {-6, 6, 0, 0, 6, -6, 0, 0, -3, -3, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -4, 4, 0, 0, -2, 2, 0, 0, -2, -2, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, -1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, -9, 0, 0, -9, 9, 0, 0, 6, 3, 0, 0, -6, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, -6, 0, 0, 3, -3, 0, 0, 4, 2, 0, 0, 2, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -6, 6, 0, 0, 6, -6, 0, 0, -3, -3, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -4, 4, 0, 0, -2, 2, 0, 0, -2, -2, 0, 0, -1, -1, 0, 0},
            {9, 0, 0, -9, -9, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 3, -6, 0, 0, -3, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, -6, 3, 0, 0, -3, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 2, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, -9, -9, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 3, -6, 0, 0, -3, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, -6, 3, 0, 0, -3, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 2, 2, 0, 0, 1},
            {-27, 27, -27, 27, 27, -27, 27, -27, -18, -9, 9, 18, 18, 9, -9, -18, -18, 18, 9, -9, 18, -18, -9, 9, -12, -6, -3, -6, 12, 6, 3, 6, -18, 18, -18, 18, -9, 9, -9, 9, -12, -6, 6, 12, -6, -3, 3, 6, -12, 12, 6, -6, -6, 6, 3, -3, -8, -4, -2, -4, -4, -2, -1, -2},
            {18, -18, 18, -18, -18, 18, -18, 18, 9, 9, -9, -9, -9, -9, 9, 9, 12, -12, -6, 6, -12, 12, 6, -6, 6, 6, 3, 3, -6, -6, -3, -3, 12, -12, 12, -12, 6, -6, 6, -6, 6, 6, -6, -6, 3, 3, -3, -3, 8, -8, -4, 4, 4, -4, -2, 2, 4, 4, 2, 2, 2, 2, 1, 1},
            {-6, 0, 0, 6, 6, 0, 0, -6, 0, 0, 0, 0, 0, 0, 0, 0, -3, 0, 0, -3, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, -4, 0, 0, 4, -2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, -2, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, -6, 0, 0, 6, 6, 0, 0, -6, 0, 0, 0, 0, 0, 0, 0, 0, -3, 0, 0, -3, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, -4, 0, 0, 4, -2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, -2, -1, 0, 0, -1},
            {18, -18, 18, -18, -18, 18, -18, 18, 12, 6, -6, -12, -12, -6, 6, 12, 9, -9, -9, 9, -9, 9, 9, -9, 6, 3, 3, 6, -6, -3, -3, -6, 12, -12, 12, -12, 6, -6, 6, -6, 8, 4, -4, -8, 4, 2, -2, -4, 6, -6, -6, 6, 3, -3, -3, 3, 4, 2, 2, 4, 2, 1, 1, 2},
            {-12, 12, -12, 12, 12, -12, 12, -12, -6, -6, 6, 6, 6, 6, -6, -6, -6, 6, 6, -6, 6, -6, -6, 6, -3, -3, -3, -3, 3, 3, 3, 3, -8, 8, -8, 8, -4, 4, -4, 4, -4, -4, 4, 4, -2, -2, 2, 2, -4, 4, 4, -4, -2, 2, 2, -2, -2, -2, -2, -2, -1, -1, -1, -1},
            {2, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {-6, 6, 0, 0, 6, -6, 0, 0, -4, -2, 0, 0, 4, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, 3, 0, 0, -3, 3, 0, 0, -2, -1, 0, 0, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {4, -4, 0, 0, -4, 4, 0, 0, 2, 2, 0, 0, -2, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, -2, 0, 0, 2, -2, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -6, 6, 0, 0, 6, -6, 0, 0, -4, -2, 0, 0, 4, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, 3, 0, 0, -3, 3, 0, 0, -2, -1, 0, 0, -2, -1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, -4, 0, 0, -4, 4, 0, 0, 2, 2, 0, 0, -2, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, -2, 0, 0, 2, -2, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {-6, 0, 0, 6, 6, 0, 0, -6, 0, 0, 0, 0, 0, 0, 0, 0, -4, 0, 0, -2, 4, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, -3, 0, 0, 3, -3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, -1, -2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, -6, 0, 0, 6, 6, 0, 0, -6, 0, 0, 0, 0, 0, 0, 0, 0, -4, 0, 0, -2, 4, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, -3, 0, 0, 3, -3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, -1, -2, 0, 0, -1},
            {18, -18, 18, -18, -18, 18, -18, 18, 12, 6, -6, -12, -12, -6, 6, 12, 12, -12, -6, 6, -12, 12, 6, -6, 8, 4, 2, 4, -8, -4, -2, -4, 9, -9, 9, -9, 9, -9, 9, -9, 6, 3, -3, -6, 6, 3, -3, -6, 6, -6, -3, 3, 6, -6, -3, 3, 4, 2, 1, 2, 4, 2, 1, 2},
            {-12, 12, -12, 12, 12, -12, 12, -12, -6, -6, 6, 6, 6, 6, -6, -6, -8, 8, 4, -4, 8, -8, -4, 4, -4, -4, -2, -2, 4, 4, 2, 2, -6, 6, -6, 6, -6, 6, -6, 6, -3, -3, 3, 3, -3, -3, 3, 3, -4, 4, 2, -2, -4, 4, 2, -2, -2, -2, -1, -1, -2, -2, -1, -1},
            {4, 0, 0, -4, -4, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, -2, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, -2, 2, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, -4, -4, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, -2, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, -2, 2, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1},
            {-12, 12, -12, 12, 12, -12, 12, -12, -8, -4, 4, 8, 8, 4, -4, -8, -6, 6, 6, -6, 6, -6, -6, 6, -4, -2, -2, -4, 4, 2, 2, 4, -6, 6, -6, 6, -6, 6, -6, 6, -4, -2, 2, 4, -4, -2, 2, 4, -3, 3, 3, -3, -3, 3, 3, -3, -2, -1, -1, -2, -2, -1, -1, -2},
            {8, -8, 8, -8, -8, 8, -8, 8, 4, 4, -4, -4, -4, -4, 4, 4, 4, -4, -4, 4, -4, 4, 4, -4, 2, 2, 2, 2, -2, -2, -2, -2, 4, -4, 4, -4, 4, -4, 4, -4, 2, 2, -2, -2, 2, 2, -2, -2, 2, -2, -2, 2, 2, -2, -2, 2, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        System.out.println("3d calc coeffs start");
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < m - 1; j++) {
                for (int k = 0; k < l - 1; k++) {
                    double func[] = new double[]{
                        f[i][j][k],
                        f[i + 1][j][k],
                        f[i + 1][j + 1][k],
                        f[i][j + 1][k],
                        f[i][j][k + 1],
                        f[i + 1][j][k + 1],
                        f[i + 1][j + 1][k + 1],
                        f[i][j + 1][k + 1],
                        d.dx[i][j][k],
                        d.dx[i + 1][j][k],
                        d.dx[i + 1][j + 1][k],
                        d.dx[i][j + 1][k],
                        d.dx[i][j][k + 1],
                        d.dx[i + 1][j][k + 1],
                        d.dx[i + 1][j + 1][k + 1],
                        d.dx[i][j + 1][k + 1],
                        d.dy[i][j][k],
                        d.dy[i + 1][j][k],
                        d.dy[i + 1][j + 1][k],
                        d.dy[i][j + 1][k],
                        d.dy[i][j][k + 1],
                        d.dy[i + 1][j][k + 1],
                        d.dy[i + 1][j + 1][k + 1],
                        d.dy[i][j + 1][k + 1],
                        d.dxy[i][j][k],
                        d.dxy[i + 1][j][k],
                        d.dxy[i + 1][j + 1][k],
                        d.dxy[i][j + 1][k],
                        d.dxy[i][j][k + 1],
                        d.dxy[i + 1][j][k + 1],
                        d.dxy[i + 1][j + 1][k + 1],
                        d.dxy[i][j + 1][k + 1],
                        d.dz[i][j][k],
                        d.dz[i + 1][j][k],
                        d.dz[i + 1][j + 1][k],
                        d.dz[i][j + 1][k],
                        d.dz[i][j][k + 1],
                        d.dz[i + 1][j][k + 1],
                        d.dz[i + 1][j + 1][k + 1],
                        d.dz[i][j + 1][k + 1],
                        d.dxz[i][j][k],
                        d.dxz[i + 1][j][k],
                        d.dxz[i + 1][j + 1][k],
                        d.dxz[i][j + 1][k],
                        d.dxz[i][j][k + 1],
                        d.dxz[i + 1][j][k + 1],
                        d.dxz[i + 1][j + 1][k + 1],
                        d.dxz[i][j + 1][k + 1],
                        d.dyz[i][j][k],
                        d.dyz[i + 1][j][k],
                        d.dyz[i + 1][j + 1][k],
                        d.dyz[i][j + 1][k],
                        d.dyz[i][j][k + 1],
                        d.dyz[i + 1][j][k + 1],
                        d.dyz[i + 1][j + 1][k + 1],
                        d.dyz[i][j + 1][k + 1],
                        d.dxyz[i][j][k],
                        d.dxyz[i + 1][j][k],
                        d.dxyz[i + 1][j + 1][k],
                        d.dxyz[i][j + 1][k],
                        d.dxyz[i][j][k + 1],
                        d.dxyz[i + 1][j][k + 1],
                        d.dxyz[i + 1][j + 1][k + 1],
                        d.dxyz[i][j + 1][k + 1]
                    };
                    polynoms[i][j][k] = new polynom_triCubic(matrix, func);
                    System.out.println(i + " " + j + " " + k);
                }
            }
        }
        System.out.println("3d calc coeffs start");
    }

    public double calc(double x, double y, double z) {

        int ix = (int) x;
        if (ix >= n - 1) {
            ix = n - 2;
        }
        double xx = (x - ix);

        int iy = (int) y;
        if (iy >= m - 1) {
            iy = m - 2;
        }
        double yy = (y - iy);

        int iz = (int) z;
        if (iz >= l - 1) {
            iz = l - 2;
        }
        double zz = (z - iz);

        return polynoms[ix][iy][iz].calc(xx, yy, zz);
    }

    public double[][][] resample(int nk, int mk, int lk) {
        double[][][] res = new double[nk][mk][lk];
        for (int i = 0; i < nk; i++) {
            for (int j = 0; j < mk; j++) {
                for (int k = 0; k < lk; k++) {
                    res[i][j][k] = calc(
                            (double) (n * i) / (nk - 1),
                            (double) (m * j) / (mk - 1),
                            (double) (l * k) / (lk - 1));
                }
            }
        }
        return res;
    }

    public Polynom getPolynom(int i, int j, int k) {
        return polynoms[i][j][k].getPolynom();
    }

    public Polynom[][][] getPolynoms() {
        Polynom[][][] result = new Polynom[n - 1][m - 1][l - 1];
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < m - 1; j++) {
                for (int k = 0; k < l - 1; k++) {
                    result[i][j][k] = polynoms[i][j][k].getPolynom();
                }
            }
        }
        return result;
    }
}
