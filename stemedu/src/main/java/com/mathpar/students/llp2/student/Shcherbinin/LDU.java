/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mathpar.students.llp2.student.Shcherbinin;

import java.util.Random;
import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.Ring;

/**
 *
 * @author scherbinin
 */
public class LDU {
    int n;
    public Element[] d;
    public MatrixS L;
    public MatrixS D;
    public MatrixS U;
    public MatrixS M;
    public MatrixS W;
    static Ring ring = Ring.ringZxyz;
    public LDU(MatrixS T){
        n = T.size;
        L = new MatrixS();
        D = new MatrixS();
        U = new MatrixS();
        M = new MatrixS();
        W = new MatrixS();
    };
    public void getLDU(MatrixS T,Element a){
        switch (n) {
            case 1:
                Element a_n = T.getElement(0, 0, ring);
                L = new MatrixS(a_n);
                D = new MatrixS(a_n).inverse(ring);
                U = new MatrixS(a_n);
                M = new MatrixS(a.multiply(a, ring));
                W = new MatrixS(a.multiply(a, ring));
                d = new Element[]{a_n};
                break;
            case 2:
                Element a_n_1 = T.getElement(0, 0, ring);
                Element a_n_0 = T.det(ring).divide(a, ring);
                Element gamma = T.getElement(1, 0, ring);
                Element betta = T.getElement(0, 1, ring);
                d = new Element[]{a_n_1,a_n_0};
                Element[][] tmp0 = {{a_n_1,ring.numberZERO},
                                    {gamma,a_n_0          }
                };
                L = new MatrixS(tmp0, ring);
                D = getD(d,a).cancel(ring);
                Element[][] tmp1 = {{a_n_1,             betta},
                                    {ring.numberZERO,   a_n_0}
                };
                U = new MatrixS(tmp1, ring);
                Element[][] tmp2 = {{a,                                        ring.numberZERO},
                                    {gamma.multiply(ring.numberMINUS_ONE,ring),a_n_1          }
                };
                M = new MatrixS(tmp2, ring);
                Element[][] tmp3 = {{a,betta.multiply(ring.numberMINUS_ONE, ring)},
                                    {ring.numberZERO,       a_n_1}
                };
                W = new MatrixS(tmp3, ring);
                break;
            default:
                MatrixS[] A = T.split();
                LDU first = new LDU(A[0]);
                first.getLDU(A[0], a);
                Element as = first.d[first.d.length-1];
                MatrixS Ukssn = first.M.multiply(A[1], ring)
                    .divideByNumber(a, ring);
                MatrixS Lkssn = A[2].multiply(first.W, ring)
                    .divideByNumber(a, ring);
                MatrixS DU = first.D.multiply(Ukssn, ring);
                MatrixS Ldu = Lkssn.multiply(DU, ring).multiplyByNumber(as, ring);
                MatrixS Asn = A[3].multiplyByNumber(as, ring);
                        Asn = Asn.subtract(Ldu, ring)
                        .divideByNumber(a, ring);
                LDU second = new LDU(Asn);
                second.getLDU(Asn, as);
                d = new Element[first.d.length+second.d.length];
                System.arraycopy(first.d, 0, d, 0, first.d.length);
                System.arraycopy(second.d, 0, d, first.d.length, second.d.length);
                MatrixS Mkssn_0 = second.M.multiply(Lkssn, ring);
                MatrixS Wkssn_0 = first.W.multiply(DU, ring);
                MatrixS DM = first.D.multiply(first.M, ring);
                MatrixS Mkssn = Mkssn_0.multiply(DM, ring).divideByNumber(a.multiply(ring.numberMINUS_ONE, ring), ring);
                MatrixS Wkssn = Wkssn_0.multiply(second.W, ring).divideByNumber(a.multiply(ring.numberMINUS_ONE, ring), ring);

                //result
                D = getD(d,a).cancel(ring);
                L = MatrixS.join(new MatrixS[]{first.L,MatrixS.zeroMatrix(first.L.size),Lkssn,second.L});
                U = MatrixS.join(new MatrixS[]{first.U,Ukssn,MatrixS.zeroMatrix(first.U.size),second.U});
                M = MatrixS.join(new MatrixS[]{first.M
                        ,
                        MatrixS.zeroMatrix(first.M.size),Mkssn,
                        second.M
                });

                W = MatrixS.join(new MatrixS[]{first.W
                        ,Wkssn,MatrixS.zeroMatrix(first.W.size),
                        second.W

                });
        }
    };
    public static MatrixS[] getLDU(MatrixS A){
        LDU tmp = new LDU(A);
        tmp.getLDU(A, ring.numberONE);
        return new MatrixS[]{tmp.L,tmp.D,tmp.U};
    }
    public static MatrixS getD(Element[] d,Element ak){
        Element[][] m = new Element[d.length][1];
        int[][] c = new int[d.length][1];
        m[0][0] = new Fraction(ak, ak.multiply(d[0],ring));
        for (int i = 1; i < c.length; i++) {
            c[i][0]=i;
            m[i][0] = new Fraction(ak, d[i].multiply(d[i-1], ring));
        }
        return new MatrixS(d.length, d.length, m, c);
    };

      public static MatrixS genM(int k,int eM){
        Random rnd = new Random();
        int[][] tmp = new int[k][k];
                for (int i = 0; i < tmp.length; i++) {
                    for (int j = 0; j < tmp.length; j++) {
//                        while(tmp[i][j]==0){
                            int ttt = rnd.nextInt(3);
//                            System.out.println("ttt="+ttt);
                            tmp[i][j] =  (ttt==1)?0:(rnd.nextInt(eM)-eM/2);
//                            System.out.println(tmp[i][j]);
//                        }
                    }
                }
            return new MatrixS(tmp, ring);
      }
    public static void main(String[] args) {
        MatrixS tmp;
        int a= 1;
        int b= 8;
        boolean flag = true;
        int[][] test = {{7,-2,6,0,3,-9,-8,9},
                               {-4,0,0,9,6,0,3,5},
                               {6,0,7,-4,-4,-2,-3,6},
                               {3,8,0,2,0,-3,-2,-4},
                               {2,0,-7,0,-3,0,8,-5},
                               {0,0,0,6,1,7,0,0},
                               {-5,1,-3,-8,6,0,-5,0},
                               {3,0,-3,0,0,-8,0,-5}};
        tmp = new MatrixS(test, ring);
//        System.out.println("A="+tmp);
        while(flag){
            System.out.println("###############################\nTEST №"+a+"\n###############################");
            long timegen = System.currentTimeMillis();
//            tmp = genM(b, 20);
            timegen = System.currentTimeMillis()-timegen;
            long time = System.currentTimeMillis();
            System.out.println("time gen="+timegen);
            MatrixS[] tmpLDU = getLDU(tmp);
            time= System.currentTimeMillis()-time;
            MatrixS res = tmpLDU[0].multiply(tmpLDU[1], ring).multiply(tmpLDU[2], ring);

            flag = res.subtract(tmp, ring).isZero(ring);
//            System.out.println(tmpLDU[1]);
            System.out.println("result = "+flag);
            System.out.println("size="+b);
            System.out.println("time="+time);
            flag=false;
            a++;
            b*=2;
        }

    }
    public static boolean getMinors(MatrixS a) {
        for (int i = 0; i < a.size; i++) {
            Element d = a.getSubMatrix(0, i, 0, i).det(ring);
            boolean flag = d.equals(ring.numberZERO, ring);
            System.out.println("d="+d);
            if (flag) {
                return !flag;
            }
        }
        return true;

    }
}
