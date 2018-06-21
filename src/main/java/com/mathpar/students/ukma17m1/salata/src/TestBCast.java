
/*
 * To change this license header, choose License Headers in Project Properties.

 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.salata.src;
import mpi.*;
import java.io.*;
/**
 *
 * @author leximiro
 */
public class TestBCast {
    //mpirun -np 2 java -cp /home/leximiro/Desktop/stemedu/target/classes com/mathpar/students/ukma17m1/salata/src/TestBCast param1 param2
	
    public static void main(String[] args) throws MPIException, IOException,ClassNotFoundException{
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        if(myrank==0){
            bcastObject(args[0],0);
            bcastObjectArray(args,args.length,0);
        }else{
            System.out.println(bcastObject("",0));
            Object[] arr = new Object[args.length];
            bcastObjectArray(arr,args.length, 0);
            for(Object obj : arr){
                System.out.println(obj);
            }
        }
        
        
        
        MPI.Finalize();
    }
    
    public static void bcastObjectArray(Object []o,int count, int root)throws IOException, MPIException,ClassNotFoundException{
        byte []tmp=null;
        int []size=new int[1];
        int rank=MPI.COMM_WORLD.getRank();
        if (rank==root){ByteArrayOutputStream bos =
            new ByteArrayOutputStream();
            ObjectOutputStream oos =
            new ObjectOutputStream(bos);
            for (int i=0; i<count; i++) oos.writeObject(o[i]);
            tmp=bos.toByteArray();
            size[0]=tmp.length;
        }
        MPI.COMM_WORLD.bcast(size,1,MPI.INT,root);
        if (rank!=root) tmp=new byte[size[0]];
        MPI.COMM_WORLD.bcast(tmp,tmp.length,MPI.BYTE,root);
        if (rank!=root){
            ByteArrayInputStream bis =
            new ByteArrayInputStream(tmp);
            ObjectInputStream ois =
            new ObjectInputStream(bis);
            for (int i=0; i<count; i++) o[i]=ois.readObject();
        }
    }
    
    public static Object bcastObject(Object o, int root) throws IOException,MPIException, ClassNotFoundException{
        byte []tmp=null;
        int []size=new int[1];
        int rank=MPI.COMM_WORLD.getRank();
        if (rank==root){
            ByteArrayOutputStream bos =
            new ByteArrayOutputStream();
            ObjectOutputStream oos =
            new ObjectOutputStream(bos);
            oos.writeObject(o);
            tmp=bos.toByteArray();
            size[0]=tmp.length;
        }
        MPI.COMM_WORLD.bcast(size,1,MPI.INT,root);
        if (rank!=root) tmp=new byte[size[0]];
        MPI.COMM_WORLD.bcast(tmp,tmp.length,MPI.BYTE,root);
        if (rank!=root){
            ByteArrayInputStream bis =
            new ByteArrayInputStream(tmp);
            ObjectInputStream ois =
            new ObjectInputStream(bis);
            return ois.readObject();
        }
        return o;
        }
}
