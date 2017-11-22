
package com.mathpar.matrix;
import java.util.Random;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.Ring;

public class NewLDU {
    int n;
    Element[] d;
    MatrixS L;
    MatrixS D;
    MatrixS U;
    MatrixS M;
    MatrixS W;
    Ring ring = Ring.ringZxyz;
    public NewLDU(MatrixS T){
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
                M = new MatrixS(a);
                W = new MatrixS(a);
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
                D = getD(d,a);
                Element[][] tmp1 = {{a_n_1,             betta},
                                    {ring.numberZERO,   a_n_0}
                };
                U = new MatrixS(tmp1, ring);
                Element[][] tmp2 = {{a,                                        ring.numberZERO},
                                    {gamma.multiply(ring.numberMINUS_ONE,ring),a_n_1          }
                };
                M = new MatrixS(tmp2, ring);
                Element[][] tmp3 = {{a,betta.multiply(ring.numberMINUS_ONE, ring)},
                                    {ring.numberZERO,                       a_n_1}
                };
                W = new MatrixS(tmp3, ring);
                break;
            default:

                MatrixS[] A = T.split();
                MatrixS Aks = A[0];
                MatrixS Bs = A[1];
                MatrixS Cs = A[2];
                MatrixS Ds = A[3];




                NewLDU first = new NewLDU(Aks);
                first.getLDU(Aks, a);
                MatrixS Ukssn = first.M.multiply(Bs, ring).divideByNumber(a, ring);
                MatrixS Lkssn = Cs.multiply(first.W, ring).divideByNumber(a, ring);
                Element as = first.d[first.d.length-1];
                MatrixS Asn = Lkssn.multiply(first.D, ring)
                        .multiply(Ukssn, ring);
                Asn = Ds.subtract(Asn, ring)
                        .multiplyByNumber(as, ring)
                        .divideByNumber(a, ring);




                NewLDU second = new NewLDU(Asn);
                second.getLDU(Asn, as);




                d = new Element[first.d.length+second.d.length];
                System.arraycopy(first.d, 0, d, 0, first.d.length);
                System.arraycopy(second.d, 0, d, first.d.length, second.d.length);

                MatrixS Mkssn = second.M.multiply(Lkssn, ring)
                        .multiply(first.D, ring)
                        .multiply(first.M, ring)
//                        .divideByNumber(as, ring)
                        .multiplyByNumber(ring.numberMINUS_ONE, ring);
                MatrixS Wkssn = first.W.multiply(first.D, ring)
                        .multiply(Ukssn, ring)
                        .multiply(second.W, ring)
//                        .divideByNumber(as, ring)
                        .multiplyByNumber(ring.numberMINUS_ONE, ring);
                MatrixS Msn = second.M.multiplyByNumber(a, ring);
//                        .divideByNumber(as, ring);
                MatrixS Wsn = second.W.multiplyByNumber(a, ring);
//                        .divideByNumber(as, ring);
                D = getD(d,a);
                L = MatrixS.join(new MatrixS[]{first.L,MatrixS.zeroMatrix(first.L.size),Lkssn,second.L});
                U = MatrixS.join(new MatrixS[]{first.U,Ukssn,MatrixS.zeroMatrix(first.U.size),second.U});
                M = MatrixS.join(new MatrixS[]{first.M,MatrixS.zeroMatrix(first.M.size),Mkssn,Msn});
                W = MatrixS.join(new MatrixS[]{first.W,Wkssn,MatrixS.zeroMatrix(first.W.size),Wsn});
        }
    };
    public static MatrixS[] getLDU(MatrixS A){
        NewLDU tmp = new NewLDU(A);
        tmp.getLDU(A, Ring.ringZxyz.numberONE);
        return new MatrixS[]{tmp.L,tmp.D,tmp.U};
    }
    public static MatrixS getD(Element[] d,Element ak){
        Element[][] m = new Element[d.length][1];
        int[][] c = new int[d.length][1];
        m[0][0] = new Fraction(ak, ak.multiply(d[0],Ring.ringZxyz));//.multiply(Ring.ringZxyz.numberONE, Ring.ringZxyz);
        for (int i = 1; i < c.length; i++) {
            c[i][0]=i;
            m[i][0] = new Fraction(ak, d[i].multiply(d[i-1], Ring.ringZxyz));
        }
        return new MatrixS(d.length, d.length, m, c);
    };
    public static MatrixS genM(int k,int eM){
        Random rnd = new Random();
        MatrixS res =new MatrixS(Ring.ringZxyz.numberZERO);
        for (int i = 1; i < k+1; i++) {
            int[][] tmp = new int[i][i];
                for (int j = 0; j < i-1; j++) {
                    for (int l = 0; l < res.M[j].length; l++) {
                        tmp[j][res.col[j][l]] = Integer.parseInt(res.M[j][l].toString());
                    }
                }
            while(true){
                for (int j = 0; j < tmp.length-1; j++) {
                    tmp[j][tmp.length-1] = rnd.nextInt(eM);
                    tmp[tmp.length-1][j] = rnd.nextInt(eM);
                }
                tmp[tmp.length-1][tmp.length-1] = rnd.nextInt(eM);
                res = new MatrixS(tmp, Ring.ringZxyz);
                Element det = res.det(Ring.ringZxyz);
                boolean flag = (det).equals(Ring.ringZxyz.numberZERO, Ring.ringZxyz);
                if(!flag){
                    System.out.println("d"+i+"="+det);
                    break;
                }
            }
        }
        return res;
    }
    public static void main(String[] args) {
        int[][] ab = {{2, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0},
                      {0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                      {0, 0, 2, 0, 1, 2, 0, 2, 0, 1, 0, 0, 1, 0, 1, 0},
                      {0, 0, 0, 2, 2, 2, 1, 2, 1, 2, 1, 0, 0, 1, 1, 0},
                      {0, 0, 0, 0, 2, 0, 2, 1, 1, 1, 0, 0, 0, 0, 2, 1},
                      {0, 0, 0, 0, 0, 2, 0, 1, 2, 2, 1, 0, 0, 0, 0, 2},
                      {0, 0, 0, 0, 0, 0, 2, 2, 1, 0, 1, 1, 1, 1, 2, 0},
                      {0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 1, 1, 0, 0, 1, 1},
                      {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 2, 0, 0, 1, 1},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 2, 2, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 1, 1, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0},
                      {7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}
        };
        int[][] al = {{2, 2, 0, 0, 0, 1, 1, 2},
                      {0, 2, 1, 2, 1, 0, 2, 1},
                      {0, 0, 1, 1, 1, 1, 0, 1},
                      {1, 1, 2, 0, 2, 1, 0, 2},
                      {1, 0, 2, 0, 0, 1, 2, 1},
                      {1, 1, 1, 1, 1, 0, 0, 0},
                      {0, 0, 2, 1, 2, 1, 2, 1},
                      {0, 1, 1, 0, 1, 1, 2, 1}
                     };
        int[] dets0 = {2,4,4,-8,16,-24,-56,76};
        int[] dets = {1,1,-4,-4,-22,-24,-16,144,1343,4277,4710,5060,2998,-11339,-37150,-10430};
    Ring ring=new Ring("Q[]");
      MatrixS tmp = new MatrixS(ab, ring);
   //     MatrixS tmp = new MatrixS(al, ring);
////        tmp = genM(8, 40);
        System.out.println("tmp="+tmp);

        MatrixS[] tmpLDU = getLDU(tmp);
        MatrixS[] tmpLDU_ = null; //tmp.LDU(ring);
        MatrixS res = tmpLDU[0].multiply(tmpLDU[1], ring).multiply(tmpLDU[2], ring);
//        MatrixS res_ = new MatrixS();
        System.out.println("res="+res);
        System.out.println("L="+tmpLDU[0]);
//        System.out.println("L="+tmpLDU_[0]);
        System.out.println("D="+tmpLDU[1]);
//        System.out.println("D="+tmpLDU_[1]);
        System.out.println("U="+tmpLDU[2]);
//        System.out.println("U="+tmpLDU_[2]);
        System.out.println("res.subtract(tmp)"+res.subtract(tmp,ring));

    }
//    public static void getMinors(MatrixS A){
//        for (int i = 0; i < A.size; i++) {
//            MatrixS tmp = A.getSubMatrix(0, i, 0, i);
//            Element det = tmp.det(ring);
//            System.out.println("det"+(i+1)+"="+det);
//        }
//    }
//
}
