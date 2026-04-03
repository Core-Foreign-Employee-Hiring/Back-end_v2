package com.forwork.backend.api.plan.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum PlanType {
    FREE("FREE"),
    PRO("PRO");


    private final String value;

    public static PlanType from(String value) {
        return Arrays.stream(PlanType.values())
                .filter(v -> v.value.equals(value))
                .findFirst()
                .orElse(null);
    }

}
