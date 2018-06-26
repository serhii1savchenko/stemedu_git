package ua.edu.ukma.karpyshyn.domain;

public class AcousticGuitar extends Guitar {

    public AcousticGuitar(String name) {
        super(name);
    }

    @Override
    public String play() {
        return "Acoustic play " + this.getName() + " plays";
    }

}
