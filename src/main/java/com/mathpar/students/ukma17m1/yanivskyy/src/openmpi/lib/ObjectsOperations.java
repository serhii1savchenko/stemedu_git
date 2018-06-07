/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yanivskyy.src.openmpi.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;
import sun.rmi.transport.Transport;

/**
 *
 * @author z1kses
 */
public class ObjectsOperations<T> {

    public void sendObjects(List<T> a, int proc, int tag) throws MPIException {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ObjectOutputStream oos
                    = new ObjectOutputStream(bos);
            for (T obj : a) {
                oos.writeObject(obj);
            }
            bos.toByteArray();

        } catch (Exception ex) {
            Logger.getLogger(Transport.class
                    .getName()).
                    log(Level.SEVERE, null, ex);
        }
        byte[] temp = bos.toByteArray();
        ByteBuffer buf = MPI.newByteBuffer(temp.length);
        buf.put(temp);
        MPI.COMM_WORLD.iSend(buf, temp.length,
                MPI.BYTE, proc, tag);
    }

    public List<T> recvObjects(int m, int proc, int tag)
            throws MPIException {
        Status s = MPI.COMM_WORLD.probe(proc, tag);
        int n = s.getCount(MPI.BYTE);
        byte[] arr = new byte[n];
        MPI.COMM_WORLD.recv(arr, n, MPI.BYTE, proc, tag);
        List<T> res = new ArrayList<T>();
        try {
            ByteArrayInputStream bis
                    = new ByteArrayInputStream(arr);
            ObjectInputStream ois
                    = new ObjectInputStream(bis);
            for (int i = 0; i < m; i++) {
                res.add((T) ois.readObject());
            }
        } catch (Exception ex) {
            Logger.getLogger(Transport.class
                    .getName()).
                    log(Level.SEVERE, null, ex);
        }
        return res;
    }

}
