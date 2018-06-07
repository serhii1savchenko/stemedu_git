/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yanivskyy.src.openmpi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Student;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import com.mathpar.students.ukma17m1.yanivskyy.src.openmpi.lib.*;

/**
 *
 * @author z1kses
 */
public class OpenMPILab4 {

    /**
     * @param args the command line arguments
     */
    
    // mpirun -n 2 java -cp "/home/z1kses/NetBeansProjects/mpi_naukma/build/classes" "openmpi/OpenMPILab3"
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        BOperations<Student> bOperations = new BOperations<Student>();
        
        MPI.Init(args);
        
        final int size = MPI.COMM_WORLD.getSize();
        final int[] processes = new int[size];
        for (int i = 0; i < processes.length; i++) {
            processes[i] = i;
        }
        
        mpi.Group g = MPI.COMM_WORLD.getGroup().incl(processes);
        Intracomm COMM_NEW = MPI.COMM_WORLD.create(g);

        int myrank = COMM_NEW.getRank();
        List<Student> students = new ArrayList<>();

        
        if (myrank == 0) {
            students = Arrays.asList(new Student[] {new Student("Oleh", "Yanivskyy")});
            bOperations.bcastObjectArray(students, 1, 0);
        } else {
            bOperations.bcastObjectArray(students, 1, 0);
            System.out.println(Arrays.asList(students));
        }

        MPI.Finalize();
    }

}
