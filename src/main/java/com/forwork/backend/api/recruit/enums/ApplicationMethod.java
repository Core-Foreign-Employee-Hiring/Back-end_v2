package com.forwork.backend.api.recruit.enums;

public enum ApplicationMethod {
    WEBSITE("홈페이지 지원"),
    PHONE_SMS("전화/문자 지원"),
    EMAIL("이메일 지원");

    private final String displayName;

    ApplicationMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
