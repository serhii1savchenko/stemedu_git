/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yanivskyy.src.openmpi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Student;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;
import openmpi.lib.ObjectOperations;
import openmpi.lib.ObjectsOperations;
import sun.rmi.transport.Transport;

/**
 *
 * @author z1kses
 */
public class OpenMPILab3 {

    /**
     * @param args the command line arguments
     */
    
    // mpirun -n 2 java -cp "/home/z1kses/NetBeansProjects/mpi_naukma/build/classes" "openmpi/OpenMPILab3"
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        ObjectsOperations<Student> objectsOperations = new ObjectsOperations<Student>();
        
        MPI.Init(args);
        
        final int size = MPI.COMM_WORLD.getSize();
        final int[] processes = new int[size];
        for (int i = 0; i < processes.length; i++) {
            processes[i] = i;
        }
        
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(processes);
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);

        int myrank = COMM_NEW.getRank();
        List<Student> students = null;

        if (myrank == 0) {
            students = Arrays.asList(new Student[] {new Student("Oleh", "Yanivskyy")});
            objectsOperations.sendObjects(students, 1, 0);
        } else if (myrank == 1) {
            students = objectsOperations.recvObjects(1, 0, 0);
            System.out.println(Arrays.asList(students));
        }

        MPI.Finalize();
    }

}
