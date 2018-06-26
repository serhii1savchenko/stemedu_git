package ua.edu.ukma.karpyshyn.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.MPI;
import mpi.MPIException;
import sun.rmi.transport.Transport;

public class SenderService {

    public static void sendObject(Object a,
            int proc, int tag)
            throws MPIException, IOException {
        ByteArrayOutputStream bos
                = new ByteArrayOutputStream();
        ObjectOutputStream oos
                = new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE,
                proc, tag);
    }

    public static void sendArrayOfObjects(Object[] a,
            int proc, int tag) throws MPIException,
            IOException {
        for (int i = 0; i < a.length; i++) {
            sendObject(a[i], proc, tag + i);
        }
    }

    public static void sendObjects(Object[] a, int proc,
            int tag)
            throws MPIException {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ObjectOutputStream oos
                    = new ObjectOutputStream(bos);
            for (Object a1 : a) {
                oos.writeObject(a1);
            }
            bos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(Transport.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        if (bos != null) {
            byte[] temp = bos.toByteArray();
            ByteBuffer buf = MPI.newByteBuffer(temp.length);
            buf.put(temp);
            MPI.COMM_WORLD.iSend(buf, temp.length,
                    MPI.BYTE, proc, tag);
        }
    }

    public static Object bcastObject(Object o, int root)
            throws IOException, MPIException,
            ClassNotFoundException {
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
        if (tmp != null) {
            MPI.COMM_WORLD.bcast(tmp, tmp.length,
                    MPI.BYTE, root);
        }
        if (rank != root) {
            ByteArrayInputStream bis
                    = new ByteArrayInputStream(tmp);
            ObjectInputStream ois
                    = new ObjectInputStream(bis);
            return ois.readObject();
        }
        return o;
    }

    public static void bcastObjectArray(Object[] o,
            int count, int root)
            throws IOException, MPIException, ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == root) {
            ByteArrayOutputStream bos
                    = new ByteArrayOutputStream();
            ObjectOutputStream oos
                    = new ObjectOutputStream(bos);
            for (int i = 0; i < count; i++) {
                oos.writeObject(o[i]);
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
                o[i] = ois.readObject();
            }
        }
    }
}
