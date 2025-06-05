package com.forwork.backend.api.recruit.enums;

public enum WorkType {
    ONSITE("대면 근무"),
    HYBRID("혼합 근무(대면+비대면)"),
    REMOTE("비대면 근무"),
    ETC("기타");

    private final String displayName;

    WorkType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
