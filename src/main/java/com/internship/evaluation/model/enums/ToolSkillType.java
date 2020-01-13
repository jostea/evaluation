package com.internship.evaluation.model.enums;

public enum ToolSkillType {

    NEVER_USED("Never Used"),
    ELEMENTARY("Elementary"),
    MIDDLE("Middle"),
    UPPER("Upper"),
    ADVANCED("Advanced");

    private String type;

    ToolSkillType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ToolSkillType fromString(String par) {
        for (ToolSkillType val : ToolSkillType.values()) {
            if (val.type.equalsIgnoreCase(par))
                return val;
        }
        return null;
    }
}
