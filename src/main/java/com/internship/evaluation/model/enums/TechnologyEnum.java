package com.internship.evaluation.model.enums;

public enum TechnologyEnum {

    JAVA("Java"),
    DOT_NET(".NET"),
    JAVASCRIPT("JavaScript"),
    REACT_JS("React"),
    ANGULAR_JS("Angular"),
    PYTHON("Python"),
    ANDROID("Android"),
    SWIFT("Swift"),
    CPLUSPLUS("Swift"),
    KOTLIN("Kotlin");

    private String type;

    TechnologyEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static TechnologyEnum fromString(String par) {
        for (TechnologyEnum val : TechnologyEnum.values()) {
            if (val.type.equalsIgnoreCase(par))
                return val;
        }
        return null;
    }

}
