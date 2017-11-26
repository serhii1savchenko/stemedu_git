package com.mathpar.splines;

/**
 *
 * @author yuri
 */
public class spline3D_LowMem {

    private int n;
    private int m;
    private int l;
    private double[][][] f;
    derivative3D d;

    public spline3D_LowMem(double[][][] f) {
        buildTriCubic(f);
    }

    private void buildTriCubic(double[][][] f) {
        n = f.length;
        m = f[0].length;
        l = f[0][0].length;
        System.out.println("3d build derivatives start");
        this.f = f;
        d = new derivative3D(f);
        System.out.println("3d build derivatives finish");
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

        double[][] matrix = new double[][]{
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
        int i = ix, j = iy, k = iz;
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
        polynom_triCubic polynom = new polynom_triCubic(matrix, func);
        return polynom.calc(xx, yy, zz);
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
}