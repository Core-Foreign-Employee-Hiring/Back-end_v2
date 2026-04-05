package com.forwork.backend.api.plan.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum FeatureKey {
    RESUME_CREATE("RESUME_CREATE"),
    RESUME_DOWNLOAD("RESUME_DOWNLOAD"),
    AI_INTERVIEW("AI_INTERVIEW");

    private final String value;

    public static FeatureKey from(String value) {
        return Arrays.stream(FeatureKey.values())
                .filter(v -> v.value.equals(value))
                .findFirst()
                .orElse(null);
    }
}
