package com.mathpar.students.ukma17m1.moholivskyi.module1;

import java.io.Serializable;

class Monkey implements Serializable {
    boolean lovesBanana = true;

    Monkey(boolean lovesBanana) {
        this.lovesBanana = lovesBanana;
    }

    public boolean doesLoveBanana() {
        return this.lovesBanana;
    }

}