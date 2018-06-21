/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17m1.yanivskyy.src.model;

import java.io.Serializable;

/**
 *
 * @author z1kses
 */
public class Student implements Serializable{

    private String name;
    private String surName;

    public Student(String name, String surName) {
        this.name = name;
        this.surName = surName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    @Override
    public String toString() {
        return "Student{" + "name=" + name + ", surName=" + surName + '}';
    }

}
