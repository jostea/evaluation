package com.internship.evaluation.model.enums;

public enum ComplexityEnum {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard");

    private String complexity;

    public String getComplexity() {
        return complexity;
    }

    ComplexityEnum(String complexity) {
        this.complexity = complexity;
    }

    public static ComplexityEnum fromString(String par) {
        for (ComplexityEnum val : values()) {
            if (val.complexity.equalsIgnoreCase(par)) {
                return val;
            }
        }
        return null;
    }
}
