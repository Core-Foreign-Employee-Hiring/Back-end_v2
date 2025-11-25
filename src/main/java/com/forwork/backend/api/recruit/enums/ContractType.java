package com.forwork.backend.api.recruit.enums;

public enum ContractType {
    INTERN("인턴"),
    REGULAR("정규직"),
    NEWCOMER("신입"),
    EXPERIENCED("경력"),
    CONTRACT("계약직");

    private final String displayName;

    ContractType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ContractType from(String value) {
        if (value == null) return null;

        try {
            return ContractType.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}

