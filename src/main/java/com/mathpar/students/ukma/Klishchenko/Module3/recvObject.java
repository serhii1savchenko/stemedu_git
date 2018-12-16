
package com.mathpar.students.ukma.Klishchenko.Module3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import mpi.*;

public class recvObject {
    public static Object main(int proc, int tag) throws MPIException, IOException,ClassNotFoundException
    {
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
ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
ObjectInputStream ois = new ObjectInputStream(bis);
res = ois.readObject();
// ïåðåäàåì íà âûõîä ïðîöåäóðû ïîëó÷åííûé îáúåêò
return res;
}
}

//mpirun java recvObject
