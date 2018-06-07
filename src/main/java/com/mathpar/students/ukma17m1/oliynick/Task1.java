package com.mathpar.students.ukma17m1.oliynick;

import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Task1 {

    // cd ~/IdeaProjects/stemedu/out/production/com/mathpar/students/ukma17m1/oliynick
    // export PATH="$PATH:/home/$USER/openmpi-build/bin"
    // export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/home/$USER/openmpi-build/lib/"

/*

mpirun -np 4 java Task1
Starting core 3, size 4
Starting core 2, size 4
Starting core 0, size 4
Starting core 1, size 4
sending to core 1
sending to core 2
sending to core 3
Received [1528323541918, 1528323541918, 1528323541918, 1528323541918]
Received [1528323541918, 1528323541918, 1528323541918, 1528323541918]
Received [1528323541918, 1528323541918, 1528323541918, 1528323541918]


mpirun -np 8 java Task1
Starting core 7, size 8
Starting core 0, size 8
Starting core 6, size 8Starting core 1, size 8

Starting core 3, size 8
Starting core 4, size 8Starting core 5, size 8

sending to core 1
sending to core 2
sending to core 3
sending to core 4
sending to core 5
sending to core 6Starting core 2, size 8

sending to core 7
Received [1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414]Received [1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414]

Received [1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414]
Received [1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414]
Received [1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414]
Received [1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414]
Received [1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414, 1528323551414]



mpirun -np 12 java Task1
Starting core 1, size 12
Starting core 2, size 12
Starting core 6, size 12
Starting core 7, size 12
Starting core 8, size 12
Starting core 0, size 12
Starting core 9, size 12
Starting core 11, size 12Starting core 5, size 12sending to core 1
Starting core 10, size 12

Starting core 3, size 12
sending to core 2

sending to core 3
Starting core 4, size 12
sending to core 4
sending to core 5
sending to core 6
sending to core 7
sending to core 8
sending to core 9
sending to core 10
sending to core 11
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]
Received [1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380, 1528323558380]


     */

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        final int me = MPI.COMM_WORLD.getRank();
        final int size = MPI.COMM_WORLD.getSize();

        System.out.println(String.format("Starting core %d, size %d", me, size));

        if (me == 0) {

            final Object[] arr = new Object[size];

            Arrays.fill(arr, System.currentTimeMillis());

            for (int i = 1; i < size; ++i) {
                sendArrayOfObjects(arr, i, 0);
                System.out.println(String.format("sending to core %d", i));
            }
        } else {
            System.out.println(String.format("Received %s", Arrays.toString(recvArrayOfObjects(size, 0, 0))));
        }

        MPI.Finalize();
    }

    public static void sendArrayOfObjects(Object[] a, int proc, int tag) {
        for (int i = 0; i < a.length; i++)
            sendObject(a[i], proc, tag + i);
    }

    public static void sendObject(Object o, int proc, int tag) {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(o);

            final byte[] temp = bos.toByteArray();
            final ByteBuffer buf = MPI.newByteBuffer(temp.length);

            buf.put(temp);
            MPI.COMM_WORLD.iSend(buf, temp.length, MPI.BYTE, proc, tag);
        } catch (Exception ex) {
            Logger.getLogger(Task1.class.getName()).
                    log(Level.SEVERE, null, ex);

            throw new RuntimeException(ex);
        }
    }

    public static Object[] recvArrayOfObjects(int n, int proc,
                                              int tag)
            throws MPIException, IOException,
            ClassNotFoundException {
        Object[] o = new Object[n];

        for (int i = 0; i < n; i++)
            o[i] = recvObject(proc, tag + i);
        return o;
    }

    public static Object recvObject(int proc, int tag)
            throws MPIException, IOException,
            ClassNotFoundException {

        Status st = MPI.COMM_WORLD.probe(proc, tag);

        int size = st.getCount(MPI.BYTE);

        byte[] tmp = new byte[size];

        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE, proc, tag);
        Object res = null;
        ByteArrayInputStream bis =
                new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        res = ois.readObject();

        return res;
    }

    public static Object[] recvObjects(int m, int proc, int tag)
            throws MPIException {
        Status s = MPI.COMM_WORLD.probe(proc, tag);
        int n = s.getCount(MPI.BYTE);
        byte[] arr = new byte[n];
        MPI.COMM_WORLD.recv(arr, n, MPI.BYTE, proc, tag);
        Object[] res = new Object[m];
        try {
            ByteArrayInputStream bis =
                    new ByteArrayInputStream(arr);
            ObjectInputStream ois =
                    new ObjectInputStream(bis);
            for (int i = 0; i < arr.length; i++)
                res[i] = (Object) ois.readObject();
        } catch (Exception ex) {
            Logger.getLogger(Task1.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return res;
    }

}
