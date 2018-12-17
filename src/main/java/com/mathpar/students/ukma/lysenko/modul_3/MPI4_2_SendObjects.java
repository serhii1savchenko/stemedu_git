package com.mathpar.students.ukma.lysenko.modul_3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            Logger.getLogger(MPI4_2_SendObjects.class.getName()).
            log(Level.SEVERE, null, ex);
        }
        byte[] temp = bos.toByteArray();
        ByteBuffer buf = MPI.newByteBuffer(temp.length);
        buf.put(temp);
        MPI.COMM_WORLD.iSend(buf, temp.length, MPI.BYTE, proc, tag);
    }
}
/*
cd /Users/mac/Desktop/andrii_lysenko/stemedu/src/main/java
mpijavac -cp /Users/mac/Desktop/andrii_lysenko/stemedu/target/classes com/mathpar/students/ukma/lysenko/modul_3/MPI4_2_SendObjects.java

*/

