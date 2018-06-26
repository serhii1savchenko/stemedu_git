package ua.edu.ukma.karpyshyn;

import java.io.IOException;
import java.util.Arrays;
import mpi.MPI;
import mpi.MPIException;
import ua.edu.ukma.karpyshyn.domain.AcousticGuitar;
import ua.edu.ukma.karpyshyn.domain.EletricGuitar;
import ua.edu.ukma.karpyshyn.domain.Guitar;
import ua.edu.ukma.karpyshyn.service.ReceiverService;
import ua.edu.ukma.karpyshyn.service.SenderService;

public class Task2Runner {

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int tag = 0;
        int root = 3;
        MPI.COMM_WORLD.barrier();
        Guitar[] guitars = new Guitar[2];
        if (myrank == root) {
            guitars = Arrays.asList(new AcousticGuitar("Yamaha"),
                    new EletricGuitar("Gibson")).toArray(new Guitar[2]);
        }

        for (int i = 0; i < np; i++) {
            for (int j = 0; j < 2; ++j) {
                guitars[j]
                        = (Guitar) SenderService.bcastObject(guitars[j], root);
            }
        }
        for (Guitar guitar : guitars) {
            System.out.println(guitar.play() + " on proc " + myrank);
        }
        MPI.Finalize();
    }
}
