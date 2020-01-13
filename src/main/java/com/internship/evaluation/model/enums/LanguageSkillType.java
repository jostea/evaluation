package com.internship.evaluation.model.enums;

public enum LanguageSkillType {
    A1("A1"),
    A2("A2"),
    B1("B1"),
    B2("B2"),
    C1("C1"),
    C2("C2");

    private String type;

    LanguageSkillType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static LanguageSkillType fromString(String par) {
        for (LanguageSkillType val : LanguageSkillType.values()) {
            if (val.type.equalsIgnoreCase(par))
                return val;
        }
        return null;
    }
}
