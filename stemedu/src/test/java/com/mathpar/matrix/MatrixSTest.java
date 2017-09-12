package com.mathpar.matrix;

import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import org.junit.Test;
import com.mathpar.polynom.Polynom;

import static org.junit.Assert.assertTrue;

public class MatrixSTest {
    @Test
    public void adjointPolynomialShouldBeExact() {
        Ring r = new Ring("Z[p]");
        MatrixS m = new MatrixS(new Element[][] {
            {new Polynom("3p + 2", r), new Polynom("p", r)},
            {new Polynom("p", r), new Polynom("4p + 3", r)}
        }, r);

        MatrixS adj = m.adjoint(r);
        assertTrue("Element at pos (0, 1) should be polynomial -p, not fraction",
                adj.getElement(0, 1, r).equals(new Polynom("-p", r), r));
    }

    @Test
    public void adjointTest() {
        int[][] mi={{1,0,2,3},{2,0,0,1},{1,2,0,3},{1,3,0,3}};
        Ring r = new Ring("Z[]");         MatrixS m=new MatrixS(mi,r);
        Element det=m.det(r);
        MatrixS adj = m.adjoint(r);
        System.out.println("m="+adj);
        System.out.println("det="+det);
        int[][] res={{0,-6,6,-4},{0,0,10,-10},{-5,0,15,-10},{0,2,-12,8}};
        assertTrue("Somthing wrong in Matrix Adjoint",
        (adj.subtract((new MatrixS(res, r)),r)).isZero(r));
    }
}
 