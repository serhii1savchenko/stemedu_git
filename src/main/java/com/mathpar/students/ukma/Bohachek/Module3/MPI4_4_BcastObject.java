package com.mathpar.students.ukma.Bohachek.Module3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import mpi.MPI;
import mpi.MPIException;


public class MPI4_4_BcastObject {
    
    public static Object bcastObject(Object o, int root)
    throws IOException,MPIException, ClassNotFoundException{
        byte []tmp=null;
        int []size=new int[1];
        int rank=MPI.COMM_WORLD.getRank();
        if (rank==root){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            tmp=bos.toByteArray();
            size[0]=tmp.length;
        }
        MPI.COMM_WORLD.bcast(size,1,MPI.INT,root);
        if (rank!=root) tmp=new byte[size[0]];
        MPI.COMM_WORLD.bcast(tmp,tmp.length,MPI.BYTE,root);
        if (rank!=root){
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        }
        return o;
    }
    
    public static void bcastObjectArray(Object []o, int count, int root)
    throws IOException, MPIException,ClassNotFoundException{
        byte []tmp=null;
        int []size=new int[1];
        int rank=MPI.COMM_WORLD.getRank();
        if (rank==root){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            for (int i=0; i<count; i++) 
                oos.writeObject(o[i]);
            tmp=bos.toByteArray();
            size[0]=tmp.length;
            MPI.COMM_WORLD.bcast(size,1,MPI.INT,root);
            if (rank!=root) 
                tmp=new byte[size[0]];
            MPI.COMM_WORLD.bcast(tmp,tmp.length,MPI.BYTE,root);
            if (rank!=root){
                ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
                ObjectInputStream ois = new ObjectInputStream(bis);
                for (int i=0; i<count; i++) 
                    o[i]=ois.readObject();
            }
        }
    }
}
