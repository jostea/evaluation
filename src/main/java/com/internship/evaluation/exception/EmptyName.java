package com.internship.evaluation.exception;

public class EmptyName extends Exception {
    public EmptyName() {
        super("Received empty name");
    }
}
