package com.mathpar.students.ukma17m1.savchenko;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

public class SendObject {

	// mpirun -np 2 java -cp /home/serhii/Desktop/parall-progr-2017/stemedu/target/classes com/mathpar/students/ukma17m1/savchenko/SendObject
	
	/* Out:
	 * Recieved Object: Hello Send and Recieve Obects!
	 */
	
	public static void sendObject(Object a,int proc, int tag) throws MPIException, IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(a);
		byte[] tmp = bos.toByteArray();
		MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
	}

	public static Object recvObject(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {
		Status st = MPI.COMM_WORLD.probe(proc, tag);
		int size = st.getCount(MPI.BYTE);
		byte[] tmp = new byte[size];
		MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE, proc, tag);
		Object res = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
		ObjectInputStream ois = new ObjectInputStream(bis);
		res = ois.readObject();
		return res;
	}

	public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
		MPI.Init(args);

		String myObject = new String("Hello Send and Recieve Obects!");

		sendObject(myObject, 1, 1);

		String recievedObject = (String) recvObject(1, 1);

		System.out.println("Recieved Object: " + recievedObject);

		MPI.Finalize();
	}

}