package com.forwork.backend.api.member.dto;

import java.util.concurrent.ThreadLocalRandom;

public record MemberSpecEvaluationExternalResponseDTO(
        Integer experience,
        Integer certificate,
        Integer language,
        Integer career,
        Integer education,
        String analysis
) {

    public static MemberSpecEvaluationExternalResponseDTO mockEvaluationResponse() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        return new MemberSpecEvaluationExternalResponseDTO(
                random.nextInt(0, 71),
                random.nextInt(0, 71),
                random.nextInt(0, 71),
                random.nextInt(0, 71),
                random.nextInt(0, 71),
                "망함 히히"
        );
    }
}
