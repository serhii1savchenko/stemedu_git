package com.nevmmv;

import mpi.BasicType;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import java.io.*;
import java.nio.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Mykola Nevmerzhytskyi
 */
public final class Main {

    /*
    * D:\java\mpi_lab\out\production\mpi_lab>mpjrun -np 4 com.nevmmv.Main
MPJ Express (0.44) is started in the multicore configuration
Starting core 3, size 4
Starting core 1, size 4
Starting core 0, size 4
Starting core 2, size 4
sending to core 1
sending to core 2
sending to core 3
Received [1528357875236, 1528357875236, 1528357875236, 1528357875236]
Received [1528357875236, 1528357875236, 1528357875236, 1528357875236]
Received [1528357875236, 1528357875236, 1528357875236, 1528357875236]


D:\java\mpi_lab\out\production\mpi_lab>mpjrun -np 8 com.nevmmv.Main
MPJ Express (0.44) is started in the multicore configuration
Starting core 1, size 8
Starting core 5, size 8
Starting core 0, size 8
Starting core 4, size 8
Starting core 2, size 8
Starting core 7, size 8
Starting core 6, size 8
Starting core 3, size 8
sending to core 1
sending to core 2
sending to core 3
sending to core 4
sending to core 5
Received [1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444]
sending to core 6
Received [1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444]
sending to core 7
Received [1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444]
Received [1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444]
Received [1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444]
Received [1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444]
Received [1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444, 1528357920444]


D:\java\mpi_lab\out\production\mpi_lab>mpjrun -np 12 com.nevmmv.Main
MPJ Express (0.44) is started in the multicore configuration
Starting core 9, size 12
Starting core 3, size 12
Starting core 5, size 12
Starting core 8, size 12
Starting core 4, size 12
Starting core 2, size 12
Starting core 10, size 12
Starting core 11, size 12
Starting core 7, size 12
Starting core 0, size 12
Starting core 1, size 12
Starting core 6, size 12
sending to core 1
sending to core 2
sending to core 3
sending to core 4
sending to core 5
sending to core 6
sending to core 7
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
sending to core 8
sending to core 9
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
sending to core 10
sending to core 11
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
Received [1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495, 1528357955495]
*/
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        final int me = MPI.COMM_WORLD.Rank();
        final int size = MPI.COMM_WORLD.Size();
//        Logger.getLogger(Main.class.getName()).info("Rank = "+me+" ; size = "+size);

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

//        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
//             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
//
//            oos.writeObject(o);
//
//            final byte[] temp = bos.toByteArray();
//            final ByteBuffer buf = ByteBuffer.allocateDirect(temp.length); //MPI.newByteBuffer(temp.length);
//
//            buf.put(temp);
//            MPI.COMM_WORLD.Isend(buf,0, temp.length, MPI.BYTE, proc, tag);
//        } catch (Exception ex) {
//            Logger.getLogger(Main.class.getName()).
//                    log(Level.SEVERE, null, ex);
//
//            throw new RuntimeException(ex);
//        }
        /////////////////////////
        try {

            Object[] sendArr = new Object[1];
            sendArr[0] = (Object) o;
            MPI.COMM_WORLD.Isend(sendArr, 0, 1, MPI.OBJECT, proc, tag);

//                byte[] bytes = serialize(o);
//                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);


//            ByteBuffer byteBuff = ByteBuffer.wrap(bytes);//allocateDirect(bytes.length /*+ MPI.SEND_OVERHEAD* /);
//            byteBuff.put(bytes);
//            MPI.Buffer_attach(byteBuff);

//            Logger.getLogger(Main.class.getName()).
//                    log(Level.INFO, "SEND_SIZE="+ bytes.length+'\n'+String.valueOf(bytes));

//                System.out.println("Serialized to " + bytes.length);

//                MPI.COMM_WORLD.Isend(byteBuffer.put(bytes), 0, bytes.length, MPI.BYTE, proc, tag);


        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).
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

        Status st = MPI.COMM_WORLD.Probe(proc, tag);

        int size = st.Get_count(MPI.OBJECT);//count;//(MPI.BYTE);
//        Logger.getLogger(Main.class.getName()).
//                log(Level.INFO, "RECV_SIZE="+ size);
//        byte[] tmp = new byte[size];
        Object[] tmp = new Object[size];

        MPI.COMM_WORLD.Recv(tmp, 0, size, MPI.OBJECT, proc, tag);

        return tmp[0];

/*        ByteArrayInputStream bis =
                new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object res = null;
//        while (ois.read() != -1) {

            res = ois.readObject();
            //...rest of the code
//        }


        bis.close();


        return res;*/
    }

    public static Object[] recvObjects(int m, int proc, int tag)
            throws MPIException {
        Status s = MPI.COMM_WORLD.Probe(proc, tag);
        int n = s.Get_count(MPI.BYTE);
        byte[] arr = new byte[n];
        MPI.COMM_WORLD.Recv(arr, 0, n, MPI.BYTE, proc, tag);
        Object[] res = new Object[m];
        try {
            ByteArrayInputStream bis =
                    new ByteArrayInputStream(arr);
            ObjectInputStream ois =
                    new ObjectInputStream(bis);
            for (int i = 0; i < arr.length; i++)
                res[i] = (Object) ois.readObject();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return res;
    }

}
