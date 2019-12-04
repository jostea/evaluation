package com.internship.evaluation.model.enums;

public enum TaskTypeEnum {

    MULTI_CHOICE("Multi Choice Question"),
    SINGLE_CHOICE("Single Choice Question"),
    CUSTOM_QUESTION("Custom Question"),
    SQL_QUESTION("SQL question"),
    CODE_QUESTION("Code question");

    private String type;

    TaskTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static TaskTypeEnum fromString(String par) {
        for (TaskTypeEnum val : TaskTypeEnum.values()) {
            if (val.type.equalsIgnoreCase(par))
                return val;
        }
        return null;
    }

}
