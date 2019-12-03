package com.internship.evaluation.model.enums;

public enum TestStatusEnum {

    WAITING_ACTIVATION("Waiting for candidate activation"),
    TEST_STARTED("Test is started"),
    TEST_FINISHED("Test is finished");

    private String type;

    TestStatusEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static TestStatusEnum fromString(String par) {
        for (TestStatusEnum val : TestStatusEnum.values()) {
            if (val.type.equalsIgnoreCase(par))
                return val;
        }
        return null;
    }
}
