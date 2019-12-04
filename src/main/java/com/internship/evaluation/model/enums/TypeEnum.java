package com.internship.evaluation.model.enums;

public enum TypeEnum {

    MULTI_CHOICE("Multi Choice"),
    SINGLE_CHOICE("Single Choice"),
    CUSTOM_QUESTION("Custom Question");

    private String type;

    TypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static TypeEnum fromString(String par) {
        for (TypeEnum val : TypeEnum.values()) {
            if (val.type.equalsIgnoreCase(par))
                return val;
        }
        return null;
    }
}
