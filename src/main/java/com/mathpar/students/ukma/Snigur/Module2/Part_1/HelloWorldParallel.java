package com.mathpar.students.ukma.Snigur.Module2.Part_1;
import mpi.*;
    public class HelloWorldParallel {
        public static void main(String[] args)
            throws MPIException {
            //èíèöèàëèçàöèÿ ïàðàëëåëüíîé ÷àñòè
            MPI.Init(args);
            //îïðåäåëåíèå íîìåðà ïðîöåññîðà
            int myrank = MPI.COMM_WORLD.getRank();
            System.out.println("Proc num " + myrank + " Hello World");
            //çàâåðøåíèå ïàðàëëåëüíîé ÷àñòè
            MPI.Finalize();
        }
    }
//mpirun java HelloWorldParallel
// Output:
//        Proc num 1 Hello World
//        Proc num 2 Hello World
//        Proc num 0 Hello World
//        Proc num 3 Hello World
