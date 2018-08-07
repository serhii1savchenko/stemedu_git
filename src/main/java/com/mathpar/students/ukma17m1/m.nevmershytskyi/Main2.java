package com.nevmmv;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import mpi.MPI;
import mpi.MPIException;
/**
 * Created by Mykola Nevmerzhytskyi
 */
public class Main2 {

    /*
    * D:\java\mpi_lab\out\production\mpi_lab>mpjrun -np 4 com.nevmmv.Main2
MPJ Express (0.44) is started in the multicore configuration
Starting core 2, size 4
Starting core 0, size 4
Starting core 1, size 4
Starting core 3, size 4
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObjectArray():
1528359838929
1528359838929
1528359838929
1528359838929
Recieved by bcastObjectArray():
1528359838929
1528359838929
1528359838929
1528359838929
Recieved by bcastObjectArray():
1528359838929
1528359838929
1528359838929
1528359838929

D:\java\mpi_lab\out\production\mpi_lab>mpjrun -np 8 com.nevmmv.Main2
MPJ Express (0.44) is started in the multicore configuration
Starting core 0, size 8
Starting core 7, size 8
Starting core 1, size 8
Starting core 2, size 8
Starting core 5, size 8
Starting core 4, size 8
Starting core 3, size 8
Starting core 6, size 8
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObjectArray():
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
Recieved by bcastObjectArray():
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
Recieved by bcastObjectArray():
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
Recieved by bcastObjectArray():
Recieved by bcastObjectArray():
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
Recieved by bcastObjectArray():
Recieved by bcastObjectArray():
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638
1528359863638

D:\java\mpi_lab\out\production\mpi_lab>mpjrun -np 12 com.nevmmv.Main2
MPJ Express (0.44) is started in the multicore configuration
Starting core 6, size 12
Starting core 9, size 12
Starting core 2, size 12
Starting core 10, size 12
Starting core 8, size 12
Starting core 3, size 12
Starting core 4, size 12
Starting core 5, size 12
Starting core 1, size 12
Starting core 7, size 12
Starting core 11, size 12
Starting core 0, size 12
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObject(): Test
Recieved by bcastObjectArray():
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
Recieved by bcastObjectArray():
1528359886149
Recieved by bcastObjectArray():
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
Recieved by bcastObjectArray():
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
Recieved by bcastObjectArray():
1528359886149
Recieved by bcastObjectArray():
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
Recieved by bcastObject(): Test
Recieved by bcastObjectArray():
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
Recieved by bcastObjectArray():
Recieved by bcastObjectArray():
1528359886149
Recieved by bcastObjectArray():
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
Recieved by bcastObjectArray():
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149
1528359886149*/

   /* public static Object bcastObject(Object o, int root) throws IOException, MPIException, ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.Rank();
        if (rank == root) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            tmp = bos.toByteArray();
            size[0] = tmp.length;
        }
        MPI.COMM_WORLD.Bcast(size, 0, 1, MPI.INT, root);
        if (rank != root) {
            tmp = new byte[size[0]];
        }
        MPI.COMM_WORLD.Bcast(tmp, 0, tmp.length,
                MPI.BYTE, root);
        if (rank != root) {
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        }
        return o;
    }

    public static void bcastObjectArray(Object[] o, int count, int root) throws IOException, MPIException, ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.Rank();
        if (rank == root) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            for (int i = 0; i < count; i++) {
                oos.writeObject(o[i]);
            }
            tmp = bos.toByteArray();
            size[0] = tmp.length;
        }
        MPI.COMM_WORLD.Bcast(size, 0, 1, MPI.INT, root);
        if (rank != root) {
            tmp = new byte[size[0]];
        }
        MPI.COMM_WORLD.Bcast(tmp, 0, tmp.length, MPI.BYTE, root);
        if (rank != root) {
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            for (int i = 0; i < count; i++) {
                o[i] = ois.readObject();
            }
        }
    }

    public static void main(String[] args) throws MPIException, ClassNotFoundException, IOException {
        MPI.Init(args);

        final int myRank = MPI.COMM_WORLD.Rank();
        final int size = MPI.COMM_WORLD.Size();

        System.out.println(String.format("Starting core %d, size %d", myRank, size));
        String object = "Test";
        final Object[] arrayOfObjects = new Object[size];

        Arrays.fill(arrayOfObjects, System.currentTimeMillis());

        if (myRank == 0) {
            bcastObject(object, 0);

            bcastObjectArray(arrayOfObjects, arrayOfObjects.length, 0);
        } else {
            String castedObject = (String) bcastObject("", 0);
            System.out.println("Recieved by bcastObject(): " + castedObject);


            Object[] recievedArray = new Object[arrayOfObjects.length];
            bcastObjectArray(recievedArray, arrayOfObjects.length, 0);
            System.out.println("Recieved by bcastObjectArray(): ");
            for (int i = 0; i < recievedArray.length; i++) {
                System.out.println(recievedArray[i]);
            }
        }

        MPI.Finalize();
    }*/

}