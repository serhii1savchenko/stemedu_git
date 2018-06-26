package ua.edu.ukma.karpyshyn;

import java.io.IOException;
import java.util.Arrays;
import mpi.*;
import ua.edu.ukma.karpyshyn.domain.AcousticGuitar;
import ua.edu.ukma.karpyshyn.domain.EletricGuitar;
import ua.edu.ukma.karpyshyn.domain.Guitar;
import ua.edu.ukma.karpyshyn.service.ReceiverService;
import ua.edu.ukma.karpyshyn.service.SenderService;

public class Task1Runner {

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int root = 1;
        MPI.COMM_WORLD.barrier();
        if (myrank == root) {
            Guitar[] guitars = Arrays.asList(new AcousticGuitar("Yamaha"),
                    new EletricGuitar("Gibson")).toArray(new Guitar[2]);
            for (int i = 0; i < np; i++) {
                if (i != myrank) {
                    SenderService.sendArrayOfObjects(guitars, i, 10);
                }
            }
            System.out.println("Objects sent by proc " + myrank);
        } else {
            System.out.println("Start receiving by proc " + myrank);
            Object[] objects = ReceiverService.recvArrayOfObjects(1, 10);
            Guitar[] guitars = Arrays.stream(objects).toArray(Guitar[]::new);
            for (Guitar guitar : guitars) {
                System.out.println(guitar.play() + " on proc " + myrank);
            }
        }
        MPI.Finalize();
    }
}
