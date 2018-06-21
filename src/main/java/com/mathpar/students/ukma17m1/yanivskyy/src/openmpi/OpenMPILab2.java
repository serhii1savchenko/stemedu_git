/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yanivskyy.src.openmpi;

import java.io.IOException;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import com.mathpar.students.ukma17m1.yanivskyy.src.model.Student;
import com.mathpar.students.ukma17m1.yanivskyy.src.openmpi.lib.*;

/**
 *
 * @author z1kses
 */
public class OpenMPILab2 {
    
    // Input:
    // mpirun -n 4 java -cp target/classes/ "com.mathpar.students.ukma17m1.yanivskyy.src.openmpi.OpenMPILab2"
    
    // Output:
    // myrank=1, received=Student{name=Oleh, surName=Yanivskyy}
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        final int size = MPI.COMM_WORLD.getSize();
        final int[] processes = new int[size];
        for (int i = 0; i < processes.length; i++) {
            processes[i] = i;
        }
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(processes);
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);

        int myrank = COMM_NEW.getRank();
        Student student = null;

        if (myrank == 0) {
            student = new Student("Oleh", "Yanivskyy");
            ObjectOperations.sendObject(student, 1, 0);
        } else if (myrank == 1) {
            student = (Student) ObjectOperations.recvObject(0, 0);
            System.out.println("myrank=" + myrank + ", received=" + student);
        }

        MPI.Finalize();
    }

}
