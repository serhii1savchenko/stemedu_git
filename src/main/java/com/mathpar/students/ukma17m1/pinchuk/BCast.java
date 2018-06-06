package com.mathpar.students.ukma17m1.pinchuk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import mpi.MPI;
import mpi.MPIException;

public class BCast {
	
	//mpirun -np 2 java -cp /home/lida/Desktop/parall-progr-2017/Project/stemedu/target/classes/ com/mathpar/students/ukma17m1/pinchuk/BCast
	
	//Output:
	//Point. x = 1, y = 1
	//Point. x = 1, y = 1

	public static Object bcastObject(Object o, int root) throws IOException,MPIException, ClassNotFoundException{
		byte []tmp=null;
		int []size=new int[1];
		int rank=MPI.COMM_WORLD.getRank();
		if (rank==root){
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(o);
			tmp=bos.toByteArray();
			size[0]=tmp.length;
		}
		MPI.COMM_WORLD.bcast(size,1,MPI.INT,root);
		if (rank!=root) {
			tmp=new byte[size[0]];
		}
		MPI.COMM_WORLD.bcast(tmp,tmp.length,
		MPI.BYTE,root);
		if (rank!=root){
			ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
			ObjectInputStream ois = new ObjectInputStream(bis);
			return ois.readObject();
		}
		return o;
	}
	
	public static void bcastObjectArray(Object []o, int count, int root) throws IOException, MPIException,ClassNotFoundException{
		byte []tmp=null;
		int []size=new int[1];
		int rank=MPI.COMM_WORLD.getRank();
		if (rank==root){
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			for (int i=0; i<count; i++) {
				oos.writeObject(o[i]);
			}
			tmp=bos.toByteArray();
			size[0]=tmp.length;
		}
		MPI.COMM_WORLD.bcast(size,1,MPI.INT,root);
		if (rank!=root){
			tmp=new byte[size[0]];
		}
		MPI.COMM_WORLD.bcast(tmp,tmp.length,MPI.BYTE,root);
		if (rank!=root){
			ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
			ObjectInputStream ois = new ObjectInputStream(bis);
			for (int i=0; i<count; i++) {
				o[i]=ois.readObject();
			}
		}
	}
	
	public static void main(String[] args) throws MPIException, ClassNotFoundException, IOException {
		MPI.Init(args);
	
		Point point1 = new Point(1, 1);
		
		Point point2 = (Point) bcastObject(point1, 0);
		
		System.out.println(point2.toString());

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
