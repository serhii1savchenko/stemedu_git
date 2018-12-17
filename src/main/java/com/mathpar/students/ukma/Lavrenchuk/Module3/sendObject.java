
package com.mathpar.students.ukma.Lavrenchuk.Module3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import mpi.*;

public class sendObject {
    public static void main(Object a,int proc, int tag)throws MPIException, IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE,proc, tag);
    }
}

//mpirun java sendObject