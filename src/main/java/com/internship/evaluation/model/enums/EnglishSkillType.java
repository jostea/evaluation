package com.internship.evaluation.model.enums;

public enum EnglishSkillType {
    A1("A1"),
    A2("A2"),
    B1("B1"),
    B2("B2"),
    C1("C1"),
    C2("C2");

    private String type;

    EnglishSkillType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static EnglishSkillType fromString(String par) {
        for (EnglishSkillType val : EnglishSkillType.values()) {
            if (val.type.equalsIgnoreCase(par))
                return val;
        }
        return null;
    }
}
