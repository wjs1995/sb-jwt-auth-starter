package com.xiaoweiyun.auth.enums;

public enum GenderEnum {
        UNKNOWN("未写入"),
    MALE("女"),
    FEMALE("男");

    private final String description;

    GenderEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
