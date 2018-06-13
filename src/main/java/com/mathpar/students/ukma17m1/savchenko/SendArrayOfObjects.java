package com.mathpar.students.ukma17m1.savchenko;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mathpar.students.llp2.student.helloworldmpi.Transport;

import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

public class SendArrayOfObjects {

	// mpirun -np 2 java -cp /home/serhii/Desktop/parall-progr-2017/stemedu/target/classes com/mathpar/students/ukma17m1/savchenko/SendArrayOfObjects
	
	/* Out:
	 * Received array of objects: 
			One
			Two
			Three
			
	 */
	
	public static void sendObjects(Object[] a, int proc, int tag) throws MPIException {
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			for (int i = 0; i < a.length; i++) {
				oos.writeObject(a[i]);
			}
			bos.toByteArray();
		} catch (Exception ex) {
			Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
		}
		byte[] temp = bos.toByteArray();
		ByteBuffer buf = MPI.newByteBuffer(temp.length);
		buf.put(temp);
		MPI.COMM_WORLD.iSend(buf, temp.length, MPI.BYTE, proc, tag);
	}

	public static Object[] recvObjects(int m, int proc, int tag) throws MPIException {
		Status s = MPI.COMM_WORLD.probe(proc, tag);
		int n = s.getCount(MPI.BYTE);
		byte[] arr = new byte[n];
		MPI.COMM_WORLD.recv(arr, n, MPI.BYTE, proc, tag);
		Object[] res = new Object[m];
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(arr);
			ObjectInputStream ois = new ObjectInputStream(bis);
			for (int i = 0; i < arr.length; i++) {
				res[i] = (Object) ois.readObject();
			}
		} catch (Exception ex) {
			Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
		}
		return res;
	}

	public static void main(String[] args) throws MPIException {
		MPI.Init(args);

		String[] arrayOfObjects = {"One", "Two", "Three"};

		sendObjects(arrayOfObjects, 1, 1);

		Object[] recievedArrayOfObjects = recvObjects(arrayOfObjects.length, 1, 1);

		System.out.println("Received array of objects: ");
		for (int i = 0; i < recievedArrayOfObjects.length; i++) {
			System.out.println((String)recievedArrayOfObjects[i]);
		}

		MPI.Finalize();
	}

}
