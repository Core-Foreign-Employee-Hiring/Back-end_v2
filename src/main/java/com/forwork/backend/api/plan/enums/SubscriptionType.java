package com.forwork.backend.api.plan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SubscriptionType {
    ACTIVE("ACTIVE"),
    EXPIRED("EXPIRED"),
    CANCELLED("CANCELLED");

    private final String value;

    public static SubscriptionType from(String value) {
        return Arrays.stream(SubscriptionType.values())
                .filter(v -> v.value.equals(value))
                .findFirst()
                .orElse(null);
    }

}
