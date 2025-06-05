package com.forwork.backend.api.recruit.enums;

public enum ContractType {
    REGULAR("정규직"),
    CONTRACT("계약직"),
    INTERN("인턴"),
    PART_TIME("아르바이트"),
    FREELANCER("프리랜서"),
    ETC("기타");

    private final String displayName;

    ContractType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
