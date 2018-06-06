package com.mathpar.students.ukma17m1.savchenko;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mpi.MPI;
import mpi.MPIException;

public class BCastObjects {

	//mpirun -np 2 java -cp /home/serhii/Desktop/parall-progr-2017/stemedu/target/classes com/mathpar/students/ukma17m1/savchenko/BCastObjects

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
		int myRank = MPI.COMM_WORLD.getRank();

		String object = "Hello";
		String[] arrayOfObjects = {"Hello", "World"};

		if(myRank==0) {
			bcastObject(object, 0);

			bcastObjectArray(arrayOfObjects, arrayOfObjects.length, 0);
		}else {
			String castedObject = (String) bcastObject("", 0);
			System.out.println("Recieved by bcastObject(): " + castedObject);

			System.out.println("     =======     ");

			Object[] recievedArray = new Object[arrayOfObjects.length];
			bcastObjectArray(recievedArray, arrayOfObjects.length, 0);
			System.out.println("Recieved by bcastObjectArray(): ");
			for (int i=0; i<recievedArray.length; i++){
				System.out.println((String)recievedArray[i]);
			}
		}

		MPI.Finalize();
	}

}
