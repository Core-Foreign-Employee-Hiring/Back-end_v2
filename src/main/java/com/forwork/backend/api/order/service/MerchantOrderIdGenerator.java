package com.forwork.backend.api.order.service;

import java.security.SecureRandom;

public final class MerchantOrderIdGenerator {

    private static final String CHAR_POOL =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "-_=";

    private static final SecureRandom random = new SecureRandom();

    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 64;

    private MerchantOrderIdGenerator() {}

    public static String generate() {
        int length = random.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;
        return generate(length);
    }

    private static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            sb.append(CHAR_POOL.charAt(index));
        }
        return sb.toString();
    }
}
