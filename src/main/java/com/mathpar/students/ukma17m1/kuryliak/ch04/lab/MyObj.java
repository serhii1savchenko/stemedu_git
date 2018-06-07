package com.mathpar.students.ukma17m1.kuryliak.ch04.lab;

import java.io.Serializable;
import java.util.Random;

public class MyObj implements Serializable {

    private static final int MAX_INT = 100;
    private static final String[] STRS = { "lorem ipsum", "dolor sit amet", "vis harum principes te in vero",
            "corrumpit", "vel ad ludus mucius", "integre vim nisl", "posse choro ei cum", "per ut tincidunt scriptorem",
            "per autem delenit", "platonem no etiam", "nostrud", "deleniti ut sit" };

    private int intField;
    private String strField;

    public MyObj() {}

    private MyObj(int intField, String strField) {
        this.intField = intField;
        this.strField = strField;
    }

    public static MyObj randomObject() {
        Random random = new Random();
        return new MyObj(random.nextInt(MAX_INT), STRS[random.nextInt(STRS.length)]);
    }

    @Override
    public String toString() {
        return "MyObj{" +
                "intField=" + intField +
                ", strField='" + strField + '\'' +
                '}';
    }
}
