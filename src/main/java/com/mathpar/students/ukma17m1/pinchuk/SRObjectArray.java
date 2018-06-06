package com.mathpar.students.ukma17m1.pinchuk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mathpar.students.llp2.student.helloworldmpi.Transport;

import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

public class SRObjectArray {
	
	//mpirun -np 2 java -cp /home/lida/Desktop/parall-progr-2017/Project/stemedu/target/classes/ com/mathpar/students/ukma17m1/pinchuk/SRObjectArray
	
	//Output:
	//Received array: 
	//Point. x = 1, y = 1
	//Point. x = 2, y = 3
	//Point. x = 4, y = 7

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
		MPI.COMM_WORLD.iSend(buf, temp.length,
		MPI.BYTE, proc, tag);
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
		
		Point[] points1 = {new Point(1, 1), new Point(2, 3), new Point (4, 7)};
		int m = points1.length;
		
		sendObjects(points1, 1, 1);
		
		Object[] points2 = recvObjects(m, 1, 1);
		System.out.println("Received array: ");
		for (int i = 0; i < points2.length; i++) {
			System.out.println(((Point) points2[i]).toString());
		}
		
		MPI.Finalize();
	}
	
	static class Point implements Serializable{
		
		int x;
		int y;
		
		Point(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public String toString() {
			return this.getClass().getSimpleName() + ". x = " + x + ", y = " + y;
		}
		
	}


}
