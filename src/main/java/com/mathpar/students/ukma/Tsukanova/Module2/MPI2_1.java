package com.mathpar.students.ukma.Tsukanova;

import mpi.*;

public class MPI2_1 {

    public static void main(String[] args) throws MPIException
    {
            MPI.Init(args);
            int myRank = MPI.COMM_WORLD.getRank();
            System.out.println(String.format("Proc num %s Hello World", myRank));
            MPI.Finalize();
    }
}

/*
COMMAND
/home/anna/openmpi/bin/mpirun --hostfile ~/hostfile -np 4 java -cp out/production/HelloWorld MPI2_1 4

RESULT
Proc num 2 Hello World
Proc num 0 Hello World
Proc num 1 Hello World
Proc num 3 Hello World

 */
