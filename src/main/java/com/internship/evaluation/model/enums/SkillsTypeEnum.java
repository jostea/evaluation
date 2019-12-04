package com.internship.evaluation.model.enums;

public enum SkillsTypeEnum {

    TECHNICAL("Technical"),
    SOFT("Soft"),
    TOOL("Tool");

    private String type;

    SkillsTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static SkillsTypeEnum fromString(String par) {
        for (SkillsTypeEnum val : SkillsTypeEnum.values()) {
            if (val.type.equalsIgnoreCase(par))
                return val;
        }
        return null;
    }

}
