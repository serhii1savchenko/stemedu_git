/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import mpi.*;
import java.io.*;

/**
 *
 * @author leximiro
 */
public class testSendObject {
    //mpirun -np 2 java -cp /home/leximiro/Downloads/stemedu/stemedu/src/main/java/com/mathpar/students/ukma17m1/salata/src/testSendObject
    
    public static void main(String[] args)throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        String str = "some string";
        sendObject(str,1,1);
        System.out.println(recvObject(1,1));
        MPI.Finilize();
        
    }
    
    public static void sendObject(Object a,int proc, int tag)throws MPIException, IOException {
        
        ByteArrayOutputStream bos =
        new ByteArrayOutputStream();
        ObjectOutputStream oos =
        new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE,
        proc, tag);
    }
    
    public static Object recvObject(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {
        // êîìàíäà ñ÷èòûâàåò ñòàòóñ áóôåðà
        // äëÿ ïðè1⁄4ìà ñîîáùåíèÿ
        // îò ïðîöåññîðà proc ñ òåãîì tag
        Status st = MPI.COMM_WORLD.probe(proc, tag);
        // ñòàòè÷åñêèé ìåòîä êëàññà Status,
        // êîòîðûé ïîäñ÷èòûâàåò êîëè÷åñòâî ýëåìåíòîâ
        // â áóôåðå (â äàííîì ñëó÷àå MPI.BYTE)
        int size = st.getCount(MPI.BYTE);
        // ñîçäàåì áàéò-ìàññèâ
        byte[] tmp = new byte[size];
        // recv - áëîêèðóþùèé ïðè1⁄4ì ìàññèâà èç
        // áóôåðà ââîäà â ìàññèâ tmp
        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE,
        proc, tag);
        Object res = null;
        ByteArrayInputStream bis =
        new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        res = ois.readObject();
        // ïåðåäàåì íà âûõîä ïðîöåäóðû ïîëó÷åííûé îáúåêò
        return res;
       }
        
}
