package com.mathpar.students.ukma17m1.pinchuk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

public class SRObject {
	
	//mpirun -np 2 java -cp /home/lida/Desktop/parall-progr-2017/Project/stemedu/target/classes/ com/mathpar/students/ukma17m1/pinchuk/SRObject
	
	//Output:
	//Receive object:Point. x = 2, y = 3
	
	public static void sendObject(Object a, int proc, int tag) throws MPIException, IOException {
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
		
		Point point1 = new Point(2, 3);
		
		sendObject(point1, 1, 1);
		
		Point point2 = (Point) recvObject(1, 1);
		System.out.println("Receive object:" + point2.toString());
		
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
