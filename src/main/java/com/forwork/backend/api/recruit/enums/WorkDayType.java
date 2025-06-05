package com.forwork.backend.api.recruit.enums;

public enum WorkDayType {
    WEEKDAYS("평일(월~금)"),
    WEEKENDS("주말(토, 일)"),
    FULL_WEEK("주 7일(월~일)"),
    SIX_DAYS("주 6일"),

    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일"),

    ETC("기타");

    private final String displayName;

    WorkDayType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
