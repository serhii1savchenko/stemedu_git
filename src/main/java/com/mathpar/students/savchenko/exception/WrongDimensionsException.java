package com.mathpar.students.savchenko.exception;

public class WrongDimensionsException extends Exception {

    public WrongDimensionsException() {
        super("Matrix must be square!");
    }

}
