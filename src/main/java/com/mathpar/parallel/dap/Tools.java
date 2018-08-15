/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.mathpar.students.ukma17i41.bosa.parallel.engine;
package com.mathpar.parallel.dap;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import com.mathpar.students.llp2.student.helloworldmpi.Transport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

/**
 *
 * @author alla
 */
public class Tools {
    
    
    public static boolean isEmptyArray(ArrayList<DropTask>[] vokzal)
    {
        for(int i=0;i<vokzal.length; i++)
        {
            if(vokzal[i]!=null && !vokzal[i].isEmpty())
            {
                return false;
            }
        
        }
    
        return true;
    }
    public static DropTask getDropObject(int type)
    {
    DropTask drop = null;
        switch (type) {
            case 0:
                drop = new Multiply();
                break;
            case 1:
                drop = new MultiplyAdd();
                break;
            case 2:
                drop = new MultiplyMinus();
                break;
            //case 3: branch = new Inversion(); break;
            //case 4: branch = new InversionAdd(); break;
            case 5:
                drop = new Cholesky();
                break;
            case 6:
                drop = new MultiplyExtended();
                break;
        }
        return drop;
    
    }
    
    
    
     public static  MatrixS init(int n) {
        int[][] mat = new int[n][n];
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = r.nextInt(10);
            }
        }
        MatrixS matS = new MatrixS(mat,Ring.ringZxyz);
        return matS;
    }
     
     public static  MatrixS initForCholesky(int n, Ring ring) {
         
        MatrixS matrix = new MatrixS(n, n, 100,new int[]{5}, new Random(), ring.numberONE(), ring);
        /*[][] mat = new double[n][n];
                    Random r = new Random();
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            mat[i][j] = r.nextInt(10);
                        } 
                    }
                    */
                   
                     for (int i = 0; i < n; i++)
                         for (int j = 0; j < n; j++)
                         {
                             if (i < j) matrix.putElement(ring.numberZERO, i, j);
                             if (i == j) 
                             {
                                 if(matrix.getElement(i, j, ring).isZero(ring))
                                     matrix.putElement(ring.numberONE, i, j);
                             }
                         }
                    
                   
                    MatrixS res = matrix.multiply(matrix.transpose(), ring);
                    
                    return res;

     }
     
    public static  MatrixS[] concatTwoArrays(MatrixS[] mas1, MatrixS mas2[]) {
        MatrixS [] result = new MatrixS[mas1.length+mas2.length];
        int count = 0;
         
        for(int i = 0; i<mas1.length; i++) { 
            result[i] = mas1[i];
            count++;
        } 
        for(int j = 0;j<mas2.length;j++) { 
            result[count++] = mas2[j];
        }
      
        return result;
    }
    
     public static ArrayList<Integer> ArrayListCreator(int ar[]){
        ArrayList<Integer> answ=new ArrayList<Integer>();
        for (int i=0; i<ar.length; i++)
            answ.add(ar[i]);
        return answ;
    }
    
     
    public static MatrixS doZeroMatrix(int n){
       int[][] mat = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = 0;
            }
        }
        
        MatrixS matS = new MatrixS(mat,Ring.ringZxyz);
        
        return matS;
    }
    
    public static MatrixS setMinus(Element m){
       MatrixS mx = (MatrixS)m;
       Element [][]mas = mx.toScalarArray(Ring.ringZpX);
    
         
        for (int i = 0; i < mx.size; i++) {
            for (int j = 0; j < mx.size; j++) {
                System.out.println(" mas[i][j].doubleValue()= "+ mas[i][j].doubleValue());
                System.out.println(" -mas[i][j].doubleValue()= "+ -mas[i][j].doubleValue());
                mas[i][j] = new Element(-mas[i][j].doubleValue());
            }
        }
        
        MatrixS matS = new MatrixS(mas,Ring.ringZxyz);
        
        return matS;
    }
      
    
    
       public static void sendObject(Object a, int proc, int tag)throws MPIException, IOException 
    {
       
        ByteArrayOutputStream bos =new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE,proc, tag);
        System.out.println("Send Object to processor "+ proc );
       
    }
    
    public static Object recvObject(int proc, int tag) throws MPIException, IOException,ClassNotFoundException 
    {
        Status st = MPI.COMM_WORLD.probe(proc, tag);
        int size = st.getCount(MPI.BYTE);
        byte[] tmp = new byte[size];
        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE,proc, tag);
        Object res = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        res = ois.readObject();
        System.out.println("Receive Object from "+proc);
        return res;
    }
    
    
      public static void sendObjects(Object[] a, int proc, int tag)throws MPIException 
    {
        System.out.println("!!!____!!!###Try to send " + proc+"  "+tag);
  //      System.out.println( a[2]+"  "+(((MatrixS)a[0]).toString())+"   "+a[1]);
        ByteArrayOutputStream bos = null;
        try 
        {
            bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            
            for (int i = 0; i < a.length; i++)
                oos.writeObject(a[i]);
            
            bos.toByteArray();
        } 
        catch (Exception ex) {
            Logger.getLogger(Transport.class.getName()).
            log(Level.SEVERE, null, ex);
        }
        byte[] temp = bos.toByteArray();
        ByteBuffer buf = MPI.newByteBuffer(temp.length);
        buf.put(temp);
       
        MPI.COMM_WORLD.iSend(buf, temp.length, MPI.BYTE, proc, tag);
        System.out.println("Send Objects to processor "+ proc );
    }
    
   
    public static Object[] recvObjects(int m, int proc, int tag) throws MPIException 
    {
        // System.out.println("Before probe recvObjects");
        Status s = MPI.COMM_WORLD.probe(proc, tag);
        // System.out.println("After probe recvObjects");
         
        int n = s.getCount(MPI.BYTE);
        byte[] arr = new byte[n];
        MPI.COMM_WORLD.recv(arr, n, MPI.BYTE, proc, tag);
        System.out.println("Receive Objects from "+proc);
        Object[] res = new Object[m];
        try 
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(arr);
            ObjectInputStream ois = new ObjectInputStream(bis);
            for (int i = 0; i < m; i++)
            {
//                 System.out.println("!!!!i" + i);
                res[i] = (Object) ois.readObject();
  //               System.out.println("!!!!i" + i);
            }
             
        } 
        catch (Exception ex) {
            Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    
}
