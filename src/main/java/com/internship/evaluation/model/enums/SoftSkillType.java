package com.internship.evaluation.model.enums;

public enum SoftSkillType {

    VERY_RARELY("Very rarely"),
    RARELY("Rarely"),
    SOMETIMES("Sometimes"),
    OFTEN("Often"),
    VERY_OFTEN("Vey Often");

    private String type;

    SoftSkillType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static SoftSkillType fromString(String par) {
        for (SoftSkillType val : SoftSkillType.values()) {
            if (val.type.equalsIgnoreCase(par))
                return val;
        }
        return null;
    }
}
