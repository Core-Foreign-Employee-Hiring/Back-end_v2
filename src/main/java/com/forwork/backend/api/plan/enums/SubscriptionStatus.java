package com.forwork.backend.api.plan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SubscriptionStatus {
    ACTIVE("ACTIVE"),
    EXPIRED("EXPIRED"),
    CANCELLED("CANCELLED"),
    REPLACED("REPLACED");

    private final String value;

    public static SubscriptionStatus from(String value) {
        return Arrays.stream(SubscriptionStatus.values())
                .filter(v -> v.value.equals(value))
                .findFirst()
                .orElse(null);
    }

}
