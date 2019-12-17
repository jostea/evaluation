package com.internship.evaluation.exception;

public class StreamHasTasks extends Exception {
    public StreamHasTasks(String message) {
        super("Stream '" + message + "' has tasks");
    }
}
