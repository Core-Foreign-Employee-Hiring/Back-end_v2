package com.forwork.backend.api.recruit.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum SubmissionDocumentType {

    RESUME(1, "이력서"),
    COVER_LETTER(1 << 1, "자기소개서"),
    PORTFOLIO(1 << 2, "포트폴리오"),
    CAREER_DESCRIPTION(1 << 3, "경력기술서"),
    ETC(1 << 4, "기타");

    private final int bit;
    private final String displayName;

    SubmissionDocumentType(int bit, String displayName) {
        this.bit = bit;
        this.displayName = displayName;
    }

    /**
     * Enum Set -> Bit 변환
     */
    public static int toBit(Set<SubmissionDocumentType> types) {
        return types.stream()
                .mapToInt(SubmissionDocumentType::getBit)
                .reduce(0, (a, b) -> a | b);
    }

    /**
     * Bit -> Enum Set 변환
     */
    public static Set<SubmissionDocumentType> fromBit(int bitMask) {
        return Arrays.stream(values())
                .filter(type -> (bitMask & type.bit) != 0)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(SubmissionDocumentType.class)));
    }


    /**
     * 특정 값 포함 여부 체크
     */
    public static boolean contains(int bitMask, SubmissionDocumentType type) {
        return (bitMask & type.bit) != 0;
    }
}
