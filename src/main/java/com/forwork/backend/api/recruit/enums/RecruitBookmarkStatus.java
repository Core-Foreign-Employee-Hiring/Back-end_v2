package com.forwork.backend.api.recruit.enums;

public enum RecruitBookmarkStatus {
    ACTIVE("북마크됨"),
    INACTIVE("북마크 해제됨");

    private final String description;

    RecruitBookmarkStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
