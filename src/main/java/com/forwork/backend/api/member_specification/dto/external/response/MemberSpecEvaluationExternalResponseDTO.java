package com.forwork.backend.api.member_specification.dto.external.response;

public record MemberSpecEvaluationExternalResponseDTO(
        Integer experience,
        Integer certificate,
        Integer language,
        Integer career,
        Integer education,
        String analysis
) {

}