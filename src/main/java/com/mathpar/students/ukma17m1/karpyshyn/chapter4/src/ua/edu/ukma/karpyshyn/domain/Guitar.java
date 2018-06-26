package ua.edu.ukma.karpyshyn.domain;

import java.io.Serializable;

public abstract class Guitar implements Serializable{
    
    private String name;

    public Guitar(String name) {
        this.name = name;
    }
    
    public abstract String play();
    
    public String getName() {
        return name;
    }
}
