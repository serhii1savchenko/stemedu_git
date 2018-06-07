/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yanivskyy.src.openmpi.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

/**
 *
 * @author z1kses
 */
public class ObjectOperations {

    public static void sendArrayOfObjects(Object[] a, int proc, int tag) throws MPIException, IOException {
        for (int i = 0; i < a.length; i++) {
            sendObject(a[i], proc, tag + i);
        }
    }

    public static Object[] recvArrayOfObjects(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {
        Object[] o = new Object[4];
        for (int i = 0; i < 4; i++) {
            o[i] = recvObject(proc, tag + i);
        }
        return o;
    }

    public static void sendObject(Object a, int proc, int tag) throws MPIException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
    }

    public static Object recvObject(int proc, int tag)
            throws MPIException, IOException,
            ClassNotFoundException {
        Status st = MPI.COMM_WORLD.probe(proc, tag);
        int size = st.getCount(MPI.BYTE);
        byte[] tmp = new byte[size];
        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE, proc, tag);
        ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

}
