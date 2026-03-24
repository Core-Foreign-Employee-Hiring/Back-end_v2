package com.forwork.backend.api.pay.enums;

import java.util.Arrays;

public enum CashReceiptProvider {

    TOSS("TOSS");

    private final String value;

    CashReceiptProvider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CashReceiptProvider of(String value) {
        if (value == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}

