import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import java.util.logging.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;


public final class Task2 {

    public static double randomFill() {
        Random rand = new Random();
        return rand.nextInt();
    }

    public static Object bcastObject(Object o, int root)
            throws IOException, MPIException,
            ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == root) {
            ByteArrayOutputStream bos =
                    new ByteArrayOutputStream();
            ObjectOutputStream oos =
                    new ObjectOutputStream(bos);
            oos.writeObject(o);
            tmp = bos.toByteArray();
            size[0] = tmp.length;
        }
        MPI.COMM_WORLD.bcast(size, 1, MPI.INT, root);
        if (rank != root) tmp = new byte[size[0]];
        MPI.COMM_WORLD.bcast(tmp, tmp.length,
                MPI.BYTE, root);
        if (rank != root) {
            ByteArrayInputStream bis =
                    new ByteArrayInputStream(tmp);
            ObjectInputStream ois =
                    new ObjectInputStream(bis);
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
            ByteArrayOutputStream bos =
                    new ByteArrayOutputStream();
            ObjectOutputStream oos =
                    new ObjectOutputStream(bos);
            for (int i = 0; i < count; i++) oos.writeObject(o[i]);
            tmp = bos.toByteArray();
            size[0] = tmp.length;
        }
        MPI.COMM_WORLD.bcast(size, 1, MPI.INT, root);
        if (rank != root) tmp = new byte[size[0]];
        MPI.COMM_WORLD.bcast(tmp, tmp.length, MPI.BYTE, root);
        if (rank != root) {
            ByteArrayInputStream bis =
                    new ByteArrayInputStream(tmp);
            ObjectInputStream ois =
                    new ObjectInputStream(bis);
            for (int i = 0; i < count; i++) o[i] = ois.readObject();
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

    /**
     * @param args
     * @throws MPIException
     * @throws ClassNotFoundException
     * @throws IOException            docker@estimate:/tmp/module-1/out/production/module-1$ mpirun -np 4 java Task2
     *                                Received by bcastObject- test1 ---- 12
     *                                Received array of objects
     *                                test1 ---- 11
     *                                test2 ---- 12
     *                                test3 ---- 13
     *                                test4 ---- 14
     *                                test5 ---- 15
     *                                Received by bcastObject- test1 ---- 12
     *                                Received array of objects
     *                                test1 ---- 11
     *                                test2 ---- 12
     *                                test3 ---- 13
     *                                test4 ---- 14
     *                                test5 ---- 15
     *                                Received by bcastObject- test1 ---- 12
     *                                Received array of objects
     *                                test1 ---- 11
     *                                test2 ---- 12
     *                                test3 ---- 13
     *                                test4 ---- 14
     *                                test5 ---- 15
     *                                <p>
     *                                docker@estimate:/tmp/module-1/out/production/module-1$ mpirun -np 8 java Task2
     *                                Received by bcastObject- test1 ---- 12
     *                                Received array of objects
     *                                test1 ---- 11
     *                                test2 ---- 12
     *                                test3 ---- 13
     *                                test4 ---- 14
     *                                test5 ---- 15
     *                                Received by bcastObject- test1 ---- 12Received by bcastObject- test1 ---- 12
     *                                <p>
     *                                Received by bcastObject- test1 ---- 12
     *                                Received by bcastObject- test1 ---- 12
     *                                Received array of objects
     *                                test1 ---- 11
     *                                test2 ---- 12
     *                                test3 ---- 13
     *                                test4 ---- 14
     *                                test5 ---- 15
     *                                Received array of objectsReceived array of objects
     *                                test1 ---- 11
     *                                test2 ---- 12
     *                                test3 ---- 13
     *                                test4 ---- 14
     *                                test5 ---- 15
     *                                <p>
     *                                test1 ---- 11
     *                                test2 ---- 12
     *                                test3 ---- 13
     *                                test4 ---- 14
     *                                test5 ---- 15
     *                                Received by bcastObject- test1 ---- 12
     *                                Received array of objects
     *                                test1 ---- 11
     *                                test2 ---- 12
     *                                test3 ---- 13
     *                                test4 ---- 14
     *                                test5 ---- 15
     *                                Received by bcastObject- test1 ---- 12
     *                                Received array of objectsReceived array of objects
     *                                test1 ---- 11
     *                                test2 ---- 12
     *                                <p>
     *                                test3 ---- 13
     *                                test1 ---- 11
     *                                test4 ---- 14
     *                                <p>
     *                                test5 ---- 15
     *                                test2 ---- 12
     *                                test3 ---- 13
     *                                test4 ---- 14
     *                                test5 ---- 15
     */
    public static void main(String[] args) throws MPIException, ClassNotFoundException, IOException {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();

        TestObject obj = new TestObject("test1", 12);
        TestObject[] arrayOfObjects = {
                new TestObject("test1", 11),
                new TestObject("test2", 12),
                new TestObject("test3", 13),
                new TestObject("test4", 14),
                new TestObject("test5", 15)
        };

        if (rank == 0) {
            bcastObject(obj, 0);
            bcastObjectArray(arrayOfObjects, arrayOfObjects.length, 0);
        } else {
            final TestObject castedObject = (TestObject) bcastObject(new TestObject("", 0), 0);
            System.out.println("Received by bcastObject- " + castedObject);
            final TestObject[] receivedArray = new TestObject[arrayOfObjects.length];
            bcastObjectArray(receivedArray, arrayOfObjects.length, 0);
            System.out.println("Received array of objects");
            for (final TestObject rarr : receivedArray) {
                System.out.println(rarr);
            }
        }
        MPI.Finalize();
    }
}


class TestObject implements Serializable {
    private String name;
    private int number;

    TestObject(String name, int number) {

        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return this.name + " ---- " + this.number;
    }
}