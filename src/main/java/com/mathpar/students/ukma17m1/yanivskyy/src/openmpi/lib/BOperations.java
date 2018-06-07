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
import java.util.List;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author z1kses
 */
public class BOperations<T> {

    public T bcastObject(T o, int root) throws IOException, MPIException, ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == root) {
            ByteArrayOutputStream bos
                    = new ByteArrayOutputStream();
            ObjectOutputStream oos
                    = new ObjectOutputStream(bos);
            oos.writeObject(o);
            tmp = bos.toByteArray();
            size[0] = tmp.length;
        }
        MPI.COMM_WORLD.bcast(size, 1, MPI.INT, root);
        if (rank != root) {
            tmp = new byte[size[0]];
        }
        MPI.COMM_WORLD.bcast(tmp, tmp.length,
                MPI.BYTE, root);
        if (rank != root) {
            ByteArrayInputStream bis
                    = new ByteArrayInputStream(tmp);
            ObjectInputStream ois
                    = new ObjectInputStream(bis);
            return (T) ois.readObject();
        }
        return o;
    }

    public void bcastObjectArray(List<T> o, int count, int root) throws IOException, MPIException, ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == root) {
            ByteArrayOutputStream bos
                    = new ByteArrayOutputStream();
            ObjectOutputStream oos
                    = new ObjectOutputStream(bos);
            for (int i = 0; i < count; i++) {
                oos.writeObject(o.get(i));
            }
            tmp = bos.toByteArray();
            size[0] = tmp.length;
        }
        MPI.COMM_WORLD.bcast(size, 1, MPI.INT, root);
        if (rank != root) {
            tmp = new byte[size[0]];
        }
        MPI.COMM_WORLD.bcast(tmp, tmp.length, MPI.BYTE, root);
        if (rank != root) {
            ByteArrayInputStream bis
                    = new ByteArrayInputStream(tmp);
            ObjectInputStream ois
                    = new ObjectInputStream(bis);
            for (int i = 0; i < count; i++) {
                o.add((T) ois.readObject());
            }
        }
    }
}
