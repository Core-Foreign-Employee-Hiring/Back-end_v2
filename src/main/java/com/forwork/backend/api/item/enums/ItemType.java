package com.forwork.backend.api.item.enums;

import java.util.Arrays;

public enum ItemType {

    PLAN("PLAN");

    private final String value;

    ItemType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ItemType from(String value) {
        return Arrays.stream(ItemType.values())
                .filter(v -> v.value.equals(value))
                .findFirst()
                .orElse(null);
    }
}
