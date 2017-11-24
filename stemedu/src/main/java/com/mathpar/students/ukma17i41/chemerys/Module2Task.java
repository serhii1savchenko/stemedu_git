/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.chemerys;

import com.mathpar.students.llp2.student.helloworldmpi.Transport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import static com.mathpar.parallel.utils.MPITransport.recvObject;
import static com.mathpar.parallel.utils.MPITransport.sendObject;

/**
 *
 * @author finstereule
 */
/*
 to run:  
halynachemerys/openmpi/bin/mpirun --hostfile hostfile  -np 4 java -cp /home/finstereule/halynachemerys/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/chemerys/Module2Task
 */  

public class Module2Task {
    
    /*
    output:
    
Student Alex (Computer science faculty) is sending.
Object was sent.
Student Alex (Computer science faculty) was received.
    */
    private static void Task1(String args[]) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        if(MPI.COMM_WORLD.getRank() == 1)
        {
            Student student1 = new Student("Alex", "Computer science faculty");
            System.out.println("\nStudent " + student1.getStudent() + " is sending.");
            sendObject(student1, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            Student recievedStudent = (Student)recvObject(1, 1);
            System.out.println("Student " + recievedStudent.getStudent() + " was received.\n");
        }

        MPI.Finalize();
    }
    
        public static void sendObject(Object a, int proc, int tag) 
            throws MPIException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
        System.out.println("Object was sent.");
    }
        
        public static Object recvObject(int proc, int tag)
            throws MPIException, IOException, ClassNotFoundException {
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
        
        
        private static Student[] createStudentsArray(){
        Student student1 = new Student("Alex", "Computer science faculty");
        Student student2 = new Student("Anna", "Economics faculty");
        Student student3 = new Student("Olena", "Computer science faculty");
        Student[] students  = {student1, student2, student3};

        return students;
    }
        
        
    public static void showStudentsInfo(Student[] students){
        
        System.out.println("\nReceived students array:\n");
        
        for (Student student : students) {
            System.out.println(student.getStudent());
        }
    }

    public static Student[] convertObjectsToStudents(Object[] objects){
        Student[] students = new Student[objects.length];

        for (int i = 0; i < objects.length; ++i){
            students[i] = (Student) objects[i];
        }

        return students;
    }
        
    /*
    output:
    
Object was sent.
Object was sent.
Object was sent.
Array of objects was received.

Received students array:

Alex (Computer science faculty)
Anna (Economics faculty)
Olena (Computer science faculty)
    */
    
    
     public static void Task2(String args[]) 
            throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        int myRank=MPI.COMM_WORLD.getRank();

        if(myRank == 1)
        {
            Student[] studentsToSend = createStudentsArray();
            sendArrayOfObjects(studentsToSend, 3, 1);
        }
        if(myRank == 3)
        {
            Student[] studentsToRecieve = convertObjectsToStudents(recvArrayOfObjects(1,1));
            System.out.println("Array of objects was received.");
            showStudentsInfo(studentsToRecieve);
        }

        MPI.Finalize();
    }
    
    public static void sendArrayOfObjects(Object[] a, int proc, int tag)
            throws MPIException, IOException {
        for (int i = 0; i < a.length; i++) {
            sendObject(a[i], proc, tag + i);
        }
    }

    public static Object[] recvArrayOfObjects(int proc, int tag) 
            throws MPIException, IOException, ClassNotFoundException {
        Object[] o = new Object[3];
        for (int i = 0; i < 3; i++) {
            o[i] = recvObject(proc, tag + i);
        }
        return o;
    }    
    
    /*
    output:
    
    com.mathpar.students.ukma17i41.chemerys.Module2Task recvObjects
SEVERE: null
java.io.EOFException
	......

Array of objects was received

Received students array:

Alex (Computer science faculty)
Anna (Economics faculty)
Olena (Computer science faculty)

    */
        
    public static void Task3(String args[]) 
            throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        int myRank=MPI.COMM_WORLD.getRank();

        if(myRank == 1)
        {
            Student[] studentsToSent = createStudentsArray();
            sendObjects(studentsToSent, 3, 1);
        }
        if(myRank == 3)
        {
            Student[] studentsToRecieve = convertObjectsToStudents(recvObjects(3,1,1));
            System.out.println("Array of objects was received");
            showStudentsInfo(studentsToRecieve);
        }

        MPI.Finalize();
    }
    
    public static void sendObjects(Object[] a, int proc, int tag) 
            throws MPIException {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ObjectOutputStream oos
                    = new ObjectOutputStream(bos);
            for (int i = 0; i < a.length; i++) {
                oos.writeObject(a[i]);
            }
            bos.toByteArray();
        } catch (Exception ex) {
            Logger.getLogger(Transport.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        byte[] temp = bos.toByteArray();
        ByteBuffer buf = MPI.newByteBuffer(temp.length);
        buf.put(temp);
        MPI.COMM_WORLD.iSend(buf, temp.length,
                MPI.BYTE, proc, tag);
    }

    public static Object[] recvObjects(int m, int proc, int tag) 
            throws MPIException
    {
        Status s = MPI.COMM_WORLD.probe(proc, tag);
        int n = s.getCount(MPI.BYTE);
        byte[] arr = new byte[n];
        MPI.COMM_WORLD.recv(arr, n, MPI.BYTE, proc, tag);
        Object[] res = new Object[m];
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(arr);
            ObjectInputStream ois = new ObjectInputStream(bis);

            for (int i = 0; i < arr.length; i++)
                res[i] = (Object) ois.readObject();
        }
        catch (Exception ex) {
            Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }
    
    /*
    output:
    
Processor №2 has sent student Alex (Computer science faculty)
Processor №3 has sent student Alex (Computer science faculty)
Processor №1 has sent student Alex (Computer science faculty)
Processor №0 has sent student Alex (Computer science faculty)
Processor №2 has received student Alex (Computer science faculty)
Processor №3 has received student Alex (Computer science faculty)
Processor №1 has received student Alex (Computer science faculty)
Processor №0 has received student Alex (Computer science faculty)

    */
    
    public static void Task4(String args[]) 
            throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        Student studentToSend = new Student("Alex", "Computer science faculty");
        System.out.println(
                "Processor №" +
                MPI.COMM_WORLD.getRank() + 
                 " has sent student " + 
                studentToSend.getStudent());

        Student studentToReceive = (Student) bcastObject(studentToSend, MPI.COMM_WORLD.getRank());
        System.out.println(
                "Processor №" +
                MPI.COMM_WORLD.getRank() + 
                " has received student " + 
                studentToReceive.getStudent());

        MPI.Finalize();
    }
    
    public static Object bcastObject(Object o, int root) 
            throws IOException, MPIException, ClassNotFoundException {
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
        MPI.COMM_WORLD.bcast(tmp, tmp.length, MPI.BYTE, root);
        if (rank != root) {
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        }
        return o;
    }
    
    /* output:
    
Processor №1 has sent students array
Processor №2 has sent students array
Processor №0 has sent students array
Processor №3 has sent students array
Processor №1 has received students array
Processor №2 has received students array
Processor №3 has received students array
Processor №0 has received students array
    */
    
     public static void Task5(String args[]) 
            throws MPIException, IOException, ClassNotFoundException{
        MPI.Init(args);

        Student[] students = createStudentsArray();
        System.out.println(
                "Processor №" +
                MPI.COMM_WORLD.getRank() + 
                " has sent students array");

        bcastObjectArray(students, students.length, MPI.COMM_WORLD.getRank());

        System.out.println(
                "Processor №" +
                MPI.COMM_WORLD.getRank() + 
                " has received students array");

        MPI.Finalize();
    }
    
    public static void bcastObjectArray(Object[] o, int count, int root) 
            throws IOException, MPIException, ClassNotFoundException {
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
    
    
    public static void main(String[] args) 
            throws MPIException, IOException, ClassNotFoundException {
        Task1(args);
        //Task2(args);
        //Task3(args);
        //Task4(args);
        //Task5(args);
    }
        
}

class Student implements Serializable
{
    String _name;
    String _faculty;

    Student(String name, String faculty)
    {
        _name = name;
        _faculty = faculty;
    }

    public String getStudent()
    {
        return _name + " (" +_faculty + ")";
    }
}