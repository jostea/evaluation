package com.internship.evaluation.model.enums;

public enum TechnicalSkillType {

    NEVER_USED("Never Used"),
    ELEMENTARY("Elementary"),
    MIDDLE("Middle"),
    UPPER("Upper"),
    ADVANCED("Advanced");

    private String type;

    TechnicalSkillType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static TechnicalSkillType fromString(String par) {
        for (TechnicalSkillType val : TechnicalSkillType.values()) {
            if (val.type.equalsIgnoreCase(par))
                return val;
        }
        return null;
    }
}
