 
    
package com.mathpar.webWithout;
import com.mathpar.func.CanonicForms;
import com.mathpar.func.F;
import com.mathpar.func.Page;
import com.mathpar.number.*;

/**
 * This is tool for chenew_fcking task, which is written in Mathpar language. You can
 * copy-past here text of Mathpar program. \\int(7\\ln(x -5))
 *^
 * @author gennadi (7\i/2* (\ln(\abs(x+\i)))- (7\i/2* (\ln(\abs(x-\i)))))
 *                 x=-2;y=2; z=\\pi/2 ;p=x +y+z; h=\\value(\\sin(\\value(p)))
 *  (-1* \\arctg((-x)/t))/ t     a = \set((-2,1),[2,5),(5.75,6],{8});
 * a=
 */



public class Play_On_Ground { 
    public static void main(String[] args) throws Exception {
        String txt 
                = //  put your program here like this: "SPACE=Z[x, y];"---  \\exp(x)+\\exp(-x)     
                  "SPACE = Q[x,y];\n"+
                                 "\\int( \\exp(x)*\\sin(x))d x;"
            //      "WW=\\solveHDE( y^2\\d(y,x)  = xy\\d(y,x));"
          //    "l = \\Factor(g);"
                 ;
        startDebug(txt);
    }

//           Ring ring = new Ring("R64[x]");
//        Element resInt = new Integrate().integration(
//                new F("(\\ln(x+3)+\\ln(x+2)+\\ln(x+1))", ring), new Polynom("x", ring), ring);
//        Element b = new F("(\\ln(\\abs(x+1))+2\\ln(\\abs(x+2))+3*\\ln(\\abs(x+3))+x*\\ln(x^3+6x^2+11x+6))-3x", ring);
//     
    
  public  static void startDebug(String txt) throws Exception {
        String out;
        Page page = new Page(Ring.ringR64xyzt, true);
        page.ring.page=page;
        Page.addTexBlanks(new StringBuilder(txt));
        page.execution(txt, 0);
        out = page.data.section[1].toString();
        System.out.println("Result: " + out);
        String latex = page.strToTexStr(txt, false);
        System.out.println("LaTeX: " + latex);
        System.out.println(page.ring.exception.length() != 0
                ? "ring.exception: " + page.ring.exception : "No ring.exception");
                String latex1 = page.strToTexStr
        (page.data.section[0] + "\nout:\n"
                + page.data.section[1], false)
                .replaceAll("\\\\unicode\\{xB0\\}", "^{\\\\circ}\\\\!")
                 .replaceAll("([^'A-Za-z0-9\\)\\]\\}])''", "$1\\\\hbox{}")
                .replaceAll("'([^\n;]+?)'", "\\\\hbox{$1}");
        System.out.println("Latex: " + latex1);
    }
}
// SPACE=R64[x,y]; f=\\solveDESV(\\systLDE(\\sin(y^2)/\\sin(y)=\\d(y,x)));
