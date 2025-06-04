package com.forwork.backend.api.member.entity;

import lombok.Getter;

@Getter
public enum CompanyType {
    LARGE_CORPORATION("대기업"),
    MIDSIZE_COMPANY("중견기업"),
    SMALL_MEDIUM_ENTERPRISE("중소기업"),
    MICRO_BUSINESS("소상공인"),
    SOLE_PROPRIETOR("개인사업자"),
    CORPORATION("법인기업"),
    SOCIAL_ENTERPRISE("사회적기업"),
    COOPERATIVE("협동조합");

    private final String description;

    CompanyType(String description) {
        this.description = description;
    }
}