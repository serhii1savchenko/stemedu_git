package com.mathpar.students.ukma17m1.yaremko.src.lab1.model;
 

import java.awt.Color;
import java.io.Serializable;

public class Car implements Serializable {

    private String name;
    private Color color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Car{" + "name=" + name + ", color=" + color + '}';
    }
    
}
