package ua.edu.ukma.karpyshyn.domain;

public class EletricGuitar extends Guitar {

    public EletricGuitar(String name) {
        super(name);
    }

    @Override
    public String play() {
        return "Electic guitar " + this.getName() + " plays";
    }
    
}
