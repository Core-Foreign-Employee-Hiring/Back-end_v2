package com.forwork.backend.api.recruit.enums;

import lombok.Getter;

@Getter
public enum WorkDayPatternType {
    WEEKDAYS("평일(월~금)"),
    WEEKENDS("주말(토, 일)"),
    FULL_WEEK("주 7일(월~일)"),
    SIX_DAYS("주 6일");

    private final String displayName;

    WorkDayPatternType(String displayName) {
        this.displayName = displayName;
    }

}

