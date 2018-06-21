/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yanivskyy.src.openmpi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.mathpar.students.ukma17m1.yanivskyy.src.model.Student;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import com.mathpar.students.ukma17m1.yanivskyy.src.openmpi.lib.*;

/**
 *
 * @author z1kses
 */
public class OpenMPILab3 {

    // Input:
    // mpirun -n 4 java -cp target/classes/ "com.mathpar.students.ukma17m1.yanivskyy.src.openmpi.OpenMPILab3"
    
    // Output:
    // myrank=1, received=[[Student{name=Oleh, surName=Yanivskyy}]]
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
            System.out.println("myrank=" + myrank + ", received=" + Arrays.asList(students));
        }

        MPI.Finalize();
    }

}
