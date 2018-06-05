import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import java.io.*;
import java.util.logging.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public final class Task1 {


    public static double randomFill() {
        Random rand = new Random();
        return rand.nextInt();
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

    /**
     * docker@estimate:/tmp/module-1/out/production/module-1$ mpirun -np 4 java Task1
     * Running Core 1, size 4
     * Running Core 2, size 4
     * Running Core 3, size 4
     * Running Core 0, size 4
     * Sending to core 1
     * Sending to core 2
     * Sending to core 3
     * Received [8.02563346E8, 5.97594747E8, -2.21212215E8, -7.572924E7]
     * Received [8.02563346E8, 5.97594747E8, -2.21212215E8, -7.572924E7]
     * Received [8.02563346E8, 5.97594747E8, -2.21212215E8, -7.572924E7]
     * <p>
     * docker@estimate:/tmp/module-1/out/production/module-1$ mpirun -np 8 java Task1
     * Running Core 2, size 8
     * Running Core 1, size 8
     * Running Core 7, size 8
     * Running Core 4, size 8
     * Running Core 0, size 8
     * Running Core 3, size 8
     * Running Core 5, size 8
     * Running Core 6, size 8
     * <p>
     * Sending to core 1
     * Sending to core 2
     * Sending to core 3
     * Sending to core 4
     * Sending to core 5
     * Sending to core 6
     * Sending to core 7
     * Received [7.51873041E8, -4.6211823E7, -8.32334356E8, 1.462083444E9, -1.96552892E8, -5.21659485E8, -7.65558857E8, 8.7128221E8]
     * Received [7.51873041E8, -4.6211823E7, -8.32334356E8, 1.462083444E9, -1.96552892E8, -5.21659485E8, -7.65558857E8, 8.7128221E8]
     * Received [7.51873041E8, -4.6211823E7, -8.32334356E8, 1.462083444E9, -1.96552892E8, -5.21659485E8, -7.65558857E8, 8.7128221E8]
     * Received [7.51873041E8, -4.6211823E7, -8.32334356E8, 1.462083444E9, -1.96552892E8, -5.21659485E8, -7.65558857E8, 8.7128221E8]
     * Received [7.51873041E8, -4.6211823E7, -8.32334356E8, 1.462083444E9, -1.96552892E8, -5.21659485E8, -7.65558857E8, 8.7128221E8]
     * Received [7.51873041E8, -4.6211823E7, -8.32334356E8, 1.462083444E9, -1.96552892E8, -5.21659485E8, -7.65558857E8, 8.7128221E8]
     * Received [7.51873041E8, -4.6211823E7, -8.32334356E8, 1.462083444E9, -1.96552892E8, -5.21659485E8, -7.65558857E8, 8.7128221E8]
     * <p>
     * docker@estimate:/tmp/module-1/out/production/module-1$ mpirun -np 12 java Task1
     * Running Core 11, size 12
     * Running Core 10, size 12
     * Running Core 6, size 12
     * Running Core 1, size 12
     * Running Core 3, size 12
     * Running Core 2, size 12
     * Running Core 7, size 12
     * Running Core 0, size 12
     * Running Core 9, size 12
     * Running Core 8, size 12
     * Running Core 5, size 12
     * Running Core 4, size 12
     * Sending to core 1
     * Sending to core 2
     * Sending to core 3
     * Sending to core 4
     * Sending to core 5
     * Sending to core 6
     * Sending to core 7
     * Sending to core 8
     * Sending to core 9
     * Sending to core 10
     * Sending to core 11
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     * Received [4.84917699E8, 1.120252386E9, -1.98729564E9, 5.95510642E8, -1.946427203E9, -1.291599657E9, -6.65592183E8, -1.585821258E9, -7.26501803E8, 8.84523617E8, 1.478010688E9, 7.59902408E8]
     */

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        final int me = MPI.COMM_WORLD.getRank();
        final int size = MPI.COMM_WORLD.getSize();

        System.out.println(String.format("Running Core %d, size %d", me, size));

        if (me == 0) {

            final Object[] arr = new Object[size];

            for (int i = 0; i < size; i++) {
                arr[i] = randomFill();
            }

            for (int i = 1; i < size; ++i) {
                sendArrayOfObjects(arr, i, 0);
                System.out.println(String.format("Sending to core %d", i));
            }
        } else {
            System.out.println(String.format("Received %s", Arrays.toString(recvArrayOfObjects(size, 0, 0))));
        }
        MPI.Finalize();
    }
}