/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.ukma17i41.beyrak;

/**
 *
 * @author maria
 */

public class Helpers {
    
    public static Person[] fillPersonsArray(){

        Person[] persons = {new Person("Maria"),
                    new Person("Oleka"),
                    new Person("Anastasia"),
                    new Person("Kateryna")};

        return persons;
    }

    public static void showPersonsInfo(Person[] persons){
        for (Person person : persons) {
            System.out.println(person.getName());
        }
    }

    public static Person[] convertObjectsToPersons(Object[] objects){
        Person[] persons = new Person[objects.length];

        for (int i = 0; i < objects.length; ++i){
            persons[i] = (Person) objects[i];
        }

        return persons;
    }
    
}
