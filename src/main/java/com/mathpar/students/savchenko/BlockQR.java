package com.mathpar.students.savchenko;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import com.mathpar.students.savchenko.exception.WrongDimensionsException;

public class BlockQR {

    public MatrixD[] blockQR(MatrixD input, Ring ring) throws WrongDimensionsException {

        MatrixD[] ABCD = block4(input);
        MatrixD A = ABCD[0];
        MatrixD B = ABCD[1];
        MatrixD C = ABCD[2];
        MatrixD D = ABCD[3];

        MatrixD[] qrForC = SVD.givensQR(C, ring);
        // TODO


        return null;
    }

    public static MatrixD[] block4(MatrixD input) throws WrongDimensionsException {
        if ((input.rowNum() != input.colNum()) || !Utils.isPowerOfTwo(input.rowNum()))
            throw new WrongDimensionsException();

        MatrixD matrix = input.copy();
        int n = matrix.rowNum();
        int h = n/2;

        if (n == 1) {
            return new MatrixD[]{matrix};
        } else {
            Element[][] Ae = new Element[h][h];
            Element[][] Be = new Element[h][h];
            Element[][] Ce = new Element[h][h];
            Element[][] De = new Element[h][h];

            readBlock(matrix, 0, 0, Ae, h);
            readBlock(matrix, 0, h, Be, h);
            readBlock(matrix, h, 0, Ce, h);
            readBlock(matrix, h, h, De, h);

            MatrixD A = new MatrixD(Ae, 0);
            MatrixD B = new MatrixD(Be, 0);
            MatrixD C = new MatrixD(Ce, 0);
            MatrixD D = new MatrixD(De, 0);

//            System.out.println("A = " + A.toString());
//            System.out.println("B = " + B.toString());
//            System.out.println("C = " + C.toString());
//            System.out.println("D = " + D.toString());

            return new MatrixD[]{A, B, C, D};
        }
    }

    private static void readBlock(MatrixD matrix, int iOffset, int jOffset, Element[][] elements, int h) {
        for (int i = iOffset; i < (h+iOffset); i++) {
            for (int j = jOffset; j < (h+jOffset); j++) {
                elements[i-iOffset][j-jOffset] = matrix.getElement(i, j);
            }
        }
    }

    public static MatrixD insertMatrixToMatrix(MatrixD matrix, MatrixD block, int i_start, int j_start){
        block = block.copy();
        MatrixD result = matrix.copy();

        for (int i = 0; i < block.rowNum(); i++) {
            for (int j = 0; j < block.colNum(); j++) {
                result.M[i+i_start][j+j_start] = block.getElement(i, j);
            }
        }

        return result;
    }

}
