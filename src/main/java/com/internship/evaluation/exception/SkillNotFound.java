package com.internship.evaluation.exception;

public class SkillNotFound extends Exception {
    public SkillNotFound(Long id) {
        super("Skill with " + id + " id didn't found");
    }

    public SkillNotFound(String msg) {
        super("Skill specified by '" + msg + "' didn't found");
    }

    public SkillNotFound() {
        super("Skill  didn't found");
    }
}
