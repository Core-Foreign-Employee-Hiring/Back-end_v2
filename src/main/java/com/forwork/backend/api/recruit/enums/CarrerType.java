package com.forwork.backend.api.recruit.enums;

import lombok.Getter;

@Getter
public enum CarrerType {
    NEWCOMER("신입"),
    EXPERIENCED("경력"),
    NOT_SPECIFIED("경력 무관");;

    private final String displayName;

    CarrerType(String displayName) {
        this.displayName = displayName;
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
