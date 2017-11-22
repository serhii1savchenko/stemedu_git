/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.beyrak;

import java.io.Serializable;

/**
 *
 * @author maria
 */

public class Person implements Serializable
{
    String name;

    Person(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
