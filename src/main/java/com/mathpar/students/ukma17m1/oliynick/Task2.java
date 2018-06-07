package com.mathpar.students.ukma17m1.oliynick;

import mpi.MPI;
import mpi.MPIException;

import java.io.*;

public class Task2 {

	// cd ~/IdeaProjects/stemedu/out/production/com/mathpar/students/ukma17m1/oliynick
	// export PATH="$PATH:/home/$USER/openmpi-build/bin"
	// export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/home/$USER/openmpi-build/lib/"

    /*
    mpirun -np 4 java Task2
    Recieved by bcastObject(): Bcast
Received object array
B cast
Test
Recieved by bcastObject(): Bcast
Received object array
B cast
Test
Recieved by bcastObject(): Bcast
Received object array
B cast
Test

     */

    /*
mpirun -np 8 java Task2
Recieved by bcastObject(): Bcast
Received object array
B cast
Test
Recieved by bcastObject(): Bcast
Recieved by bcastObject(): Bcast
Recieved by bcastObject(): Bcast
Received object array
B cast
Test
Received object array
B cast
Test
Recieved by bcastObject(): Bcast
Received object array
B cast
Test
Recieved by bcastObject(): Bcast
Received object array
B cast
Test
Received object array
B cast
Test
Recieved by bcastObject(): Bcast
Received object array
B cast
Test

     */

    /*
mpirun -np 12 java Task2

Recieved by bcastObject(): Bcast
Recieved by bcastObject(): Bcast
Recieved by bcastObject(): Bcast
Recieved by bcastObject(): Bcast
Recieved by bcastObject(): Bcast
Recieved by bcastObject(): Bcast
Recieved by bcastObject(): Bcast
Received object array
B cast
Test
Recieved by bcastObject(): Bcast
Recieved by bcastObject(): BcastReceived object array
B cast
Test
Received object array
B cast
Test

Received object array
B cast
Test
Received object array
B cast
Test
Recieved by bcastObject(): Bcast
Received object array
B cast
Test
Received object array
B cast
Test
Recieved by bcastObject(): Bcast
Received object array
B cast
Test
Received object array
B cast
Test
Received object array
B cast
Test
Received object array
B cast
Test

     */

	public static void main(String[] args) throws MPIException, ClassNotFoundException, IOException {
		MPI.Init(args);
		int myRank = MPI.COMM_WORLD.getRank();

		String object = "Bcast";
		String[] arrayOfObjects = {"B cast", "Test"};

		if (myRank == 0) {
			bcastObject(object, 0);
			bcastObjectArray(arrayOfObjects, arrayOfObjects.length, 0);
		} else {
			final String castedObject = (String) bcastObject("", 0);

			System.out.println("Recieved by bcastObject(): " + castedObject);

			final Object[] recievedArray = new Object[arrayOfObjects.length];

			bcastObjectArray(recievedArray, arrayOfObjects.length, 0);

			System.out.println("Received object array");

			for (final Object ra : recievedArray) {
				System.out.println((String) ra);
			}
		}

		MPI.Finalize();
	}

	public static Object bcastObject(Object o, int root) throws IOException, MPIException, ClassNotFoundException {
		byte[] tmp = null;
		int[] size = new int[1];
		int rank = MPI.COMM_WORLD.getRank();
		if (rank == root) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
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
			ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
			ObjectInputStream ois = new ObjectInputStream(bis);
			return ois.readObject();
		}
		return o;
	}

	public static void bcastObjectArray(Object[] o, int count, int root) throws IOException, MPIException, ClassNotFoundException {
		byte[] tmp = null;
		int[] size = new int[1];
		int rank = MPI.COMM_WORLD.getRank();
		if (rank == root) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
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
			ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
			ObjectInputStream ois = new ObjectInputStream(bis);
			for (int i = 0; i < count; i++) {
				o[i] = ois.readObject();
			}
		}
	}


}
