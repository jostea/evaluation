package com.internship.evaluation.exception;

public class StreamNotFound extends Exception {
    public StreamNotFound(String msg) {
        super("Stream specified by " + msg + " didn't found.");
    }
}
