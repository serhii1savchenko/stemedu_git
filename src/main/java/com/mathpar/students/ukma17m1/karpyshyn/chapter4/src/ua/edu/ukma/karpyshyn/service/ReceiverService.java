package ua.edu.ukma.karpyshyn.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;
import sun.rmi.transport.Transport;

public class ReceiverService {

    public static Object recvObject(int proc, int tag)
            throws MPIException, IOException,
            ClassNotFoundException {
        Status st = MPI.COMM_WORLD.probe(proc, tag);
        int size = st.getCount(MPI.BYTE);
        byte[] tmp = new byte[size];
        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE,
                proc, tag);
        ByteArrayInputStream bis
                = new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

    public static Object[] recvArrayOfObjects(int proc,
            int tag)
            throws MPIException, IOException,
            ClassNotFoundException {
        Object[] o = new Object[2];
        for (int i = 0; i < 2; i++) {
            o[i] = recvObject(proc, tag + i);
        }
        return o;
    }

    public static Object[] recvObjects(int m, int proc, int tag)
            throws MPIException {
        Status s = MPI.COMM_WORLD.probe(proc, tag);
        int n = s.getCount(MPI.BYTE);
        byte[] arr = new byte[n];
        MPI.COMM_WORLD.recv(arr, n, MPI.BYTE, proc, tag);
        Object[] res = new Object[m];
        try {
            ByteArrayInputStream bis
                    = new ByteArrayInputStream(arr);
            ObjectInputStream ois
                    = new ObjectInputStream(bis);
            for (int i = 0; i < arr.length; i++) {
                res[i] = (Object) ois.readObject();
            }
        } catch (Exception ex) {
            Logger.getLogger(Transport.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return res;
    }
}
