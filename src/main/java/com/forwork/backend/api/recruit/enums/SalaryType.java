package com.forwork.backend.api.recruit.enums;

public enum SalaryType {
    ANNUAL("연봉"),
    MONTHLY("월급"),
    WEEKLY("주급"),
    DAILY("일급"),
    HOURLY("시급"),

    ETC("기타");

    private final String displayName;

    SalaryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
