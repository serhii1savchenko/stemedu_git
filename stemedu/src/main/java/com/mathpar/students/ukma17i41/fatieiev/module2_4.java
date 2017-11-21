/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.fatieiev;

import com.mathpar.parallel.utils.MPITransport;
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

/**
 *
 * @author ivan
 */
public class module2_4 {

    // command : 
    // /usr/bin/mpirun --hostfile hose -np 4 /home/ivan/Documents/program/stemedu/stemedu/src/main/java/com/mathpar/students/ukma17i41/fatieiev/module2 
    public static void sendObject(Object a, int proc, int tag) throws MPIException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
        System.out.println("send result");
    }

    public static Object recvObject(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {
        Status st = MPI.COMM_WORLD.probe(proc, tag);
        int size = st.getCount(MPI.BYTE);
        byte[] tmp = new byte[size];
        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE,
                proc, tag);
        Object res = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        res = ois.readObject();
        return res;
    }

    public static void sendArrayOfObjects(Object[] a, int proc, int tag) throws MPIException, IOException {
        for (int i = 0; i < a.length; i++) {
            sendObject(a[i], proc, tag + i);
        }
    }

    public static void sendObjects(Object[] a, int proc, int tag) throws MPIException {
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

    public static Object[] recvArrayOfObjects(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {
        Object[] o = new Object[4];
        for (int i = 0; i < 4; i++) {
            o[i] = recvObject(proc, tag + i);
        }
        return o;
    }

    public static Object[] recvObjects(int m, int proc, int tag) throws MPIException
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
        MPI.COMM_WORLD.bcast(tmp, tmp.length, MPI.BYTE, root);
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

    private static Fatik[] convertObjectsToFatics(Object[] objects){
        Fatik[] fats = new Fatik[objects.length];

        for (int i=0; i<objects.length; ++i){
            fats[i] = (Fatik) objects[i];
        }

        return fats;
    }
    
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        /*
        /usr/bin/mpirun --default-hostfile none  -np 4 java -cp /home/ivan/Documents/program/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/fatieiev/module2_4
        
        Output:
        
        true
        send result
        Object successfully received
        true
         */

        /*
        if(MPI.COMM_WORLD.getRank() == 1)
        {
            Fatik fat = new Fatik(true);
            System.out.println(fat.man());
            sendObject(fat, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            Fatik recievedObject = (Fatik) recvObject(1, 1);
            System.out.println("Object successfully received");
            System.out.println(recievedObject.man());
        }
        */
 
 
        Fatik fat1 = new Fatik(true);
        Fatik fat2 = new Fatik(false);
        Fatik fat3 = new Fatik(false);
        Fatik fat4 = new Fatik(true);
        Fatik[] fatsArray = {fat1,fat2,fat3,fat4};

 
        /*
        /usr/bin/mpirun --default-hostfile none  -np 4 java -cp /home/ivan/Documents/program/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/fatieiev/module2_4
        
        Output:
        
        send result
        send result
        send result
        send result
        Objects successfully received
        true
        false
        false
        true
        
        */
        
        /*
        int myRank = MPI.COMM_WORLD.getRank();

        if (myRank == 1) {

            sendArrayOfObjects(fatsArray, 3, 1);
        }
        if (myRank == 3) {
            Fatik[] recievedFats = convertObjectsToFatics(recvArrayOfObjects(1, 1));
            System.out.println("Objects successfully received");

            for (Fatik fat:recievedFats) {
                System.out.println(fat.man());
            }
        }*/
        
        
        /*
        /usr/bin/mpirun --default-hostfile none  -np 4 java -cp /home/ivan/Documents/program/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/fatieiev/module2_4

        Output:
        
        Objects successfully received
        true
        false
        false
        true
        */
        
        /*
        if(MPI.COMM_WORLD.getRank() == 1)
        {
            sendObjects(fatsArray, 3, 1);
        }
        if(MPI.COMM_WORLD.getRank() == 3)
        {
            Fatik[] recievedFats = convertObjectsToFatics(recvObjects(4,1,1));
            System.out.println("Objects successfully received");

            for (Fatik fat:recievedFats) {
                System.out.println(fat.man());
            }
        }
        */
        
        
        /*
        /usr/bin/mpirun --default-hostfile none  -np 4 java -cp /home/ivan/Documents/program/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/fatieiev/module2_4
        
        Output:
        
        true
        I'm 0 processor trying to send
        true
        I'm 3 processor trying to send
        true
        I'm 2 processor trying to send
        true
        I'm 1 processor trying to send
        I'm 3 received fatI'm 0 received fat
        true

        true
        I'm 2 received fat
        I'm 1 received fat
        true
        true
        
        */
        
        /*
        Fatik fat = new Fatik(true);
        System.out.println(fat.man());
        System.out.println("I'm " + MPI.COMM_WORLD.getRank() + " processor trying to send");

        Fatik receivedFatik = (Fatik) bcastObject(fat, MPI.COMM_WORLD.getRank());
        System.out.println("I'm " + MPI.COMM_WORLD.getRank() +" received fat");
        System.out.println(receivedFatik.man());
        */
        
        
        /*
        /usr/bin/mpirun --default-hostfile none  -np 4 java -cp /home/ivan/Documents/program/stemedu/stemedu/target/classes com/mathpar/students/ukma17i41/fatieiev/module2_4
        
        Output:
        
        Program successfully finished. My rank = 0
        Program successfully finished. My rank = 2
        Program successfully finished. My rank = 3
        Program successfully finished. My rank = 1
        
        */
        bcastObjectArray(fatsArray, fatsArray.length, MPI.COMM_WORLD.getRank());

        System.out.println("Program successfully finished. My rank = " + MPI.COMM_WORLD.getRank());

        MPI.Finalize();

    }

}

class Fatik implements Serializable {
    boolean isMan = true;

    Fatik(boolean _isMan) {
        isMan = _isMan;
    }

    public boolean man() {
        return isMan;
    }
}
