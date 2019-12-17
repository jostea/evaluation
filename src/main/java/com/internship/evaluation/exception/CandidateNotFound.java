package com.internship.evaluation.exception;

public class CandidateNotFound extends Exception {
    public CandidateNotFound(String message) {
        super("Candidate specified by '" + message + "' didn't found");
    }
}
