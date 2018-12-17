package com.mathpar.students.ukma.Melnychenko.Module3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Transport;
import mpi.MPI;
import mpi.MPIException;

import static com.mathpar.parallel.utils.MPITransport.sendObject;

public class MPI4_2_SendObjects {
    
    public static void sendArrayOfObjects(Object[] a, int proc, int tag) 
    throws MPIException, IOException {
        for (int i = 0; i < a.length; i++)
            sendObject(a[i], proc, tag + i);
    }
    
    public static void sendObjects(Object[] a, int proc, int tag)
    throws MPIException {
        ByteArrayOutputStream bos = null;
        try {bos = new ByteArrayOutputStream();
            ObjectOutputStream oos =
            new ObjectOutputStream(bos);
            for (Object a1 : a) {
                oos.writeObject(a1);
            }
            bos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(Transport.class.getName()).
            log(Level.SEVERE, null, ex);
        }
        byte[] temp = bos.toByteArray();
        ByteBuffer buf = MPI.newByteBuffer(temp.length);
        buf.put(temp);
        MPI.COMM_WORLD.iSend(buf, temp.length, MPI.BYTE, proc, tag);
    }
}

