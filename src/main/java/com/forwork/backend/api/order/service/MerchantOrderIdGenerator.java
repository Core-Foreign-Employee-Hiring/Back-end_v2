package com.forwork.backend.api.order.service;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class MerchantOrderIdGenerator {

    private static final String CHAR_POOL =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "-_=";

    private static final SecureRandom random = new SecureRandom();

    private static final int MIN_LENGTH = 12;
    private static final int MAX_LENGTH = 16;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private MerchantOrderIdGenerator() {
    }

    public static String generate() {
        // 최종 길이 결정
        int totalLength = random.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;

        // 날짜 길이
        String datePart = OffsetDateTime.now(ZoneId.of("Asia/Seoul")).format(DATE_FORMATTER);
        int dateLength = datePart.length(); // 8

        // 랜덤 문자열 길이 = 전체 길이 - 날짜 길이
        int randomLength = totalLength - dateLength;

        String randomPart = generateRandomString(randomLength);

        return datePart + randomPart;
    }

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            sb.append(CHAR_POOL.charAt(index));
        }
        return sb.toString();
    }
}
