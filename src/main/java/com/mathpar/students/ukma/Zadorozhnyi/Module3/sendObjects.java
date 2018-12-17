/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma.Zadorozhnyi.Module3;

import com.mathpar.students.llp2.student.helloworldmpi.Transport;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.*;

public class sendObjects {
    public static void main(Object[] a, int proc,int tag)throws MPIException {
    ByteArrayOutputStream bos = null;
    try 
    {
        bos = new ByteArrayOutputStream();
        ObjectOutputStream oos =
        new ObjectOutputStream(bos);
        for (int i = 0; i < a.length; i++)
        oos.writeObject(a[i]);
        bos.toByteArray();
    } 
    catch (Exception ex) 
    {
        Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
    }
    byte[] temp = bos.toByteArray();
    ByteBuffer buf = MPI.newByteBuffer(temp.length);
    buf.put(temp);
    MPI.COMM_WORLD.iSend(buf, temp.length,
    MPI.BYTE, proc, tag);
    }
}

//mpirun java sendObjects