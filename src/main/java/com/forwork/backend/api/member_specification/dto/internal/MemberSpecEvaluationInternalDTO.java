package com.forwork.backend.api.member_specification.dto.internal;

import com.forwork.backend.api.member_specification.dto.external.response.MemberSpecEvaluationExternalResponseDTO;
import org.springframework.http.HttpStatus;

public record MemberSpecEvaluationInternalDTO(
        boolean success,
        Integer experience,
        Integer certificate,
        Integer language,
        Integer career,
        Integer education,
        String analysis,
        HttpStatus httpStatus,
        String message
) {
    public static MemberSpecEvaluationInternalDTO success(MemberSpecEvaluationExternalResponseDTO dto) {
        return new MemberSpecEvaluationInternalDTO(
                true,
                dto.experience(),
                dto.certificate(),
                dto.language(),
                dto.career(),
                dto.education(),
                dto.analysis(),
                HttpStatus.OK,
                "Evaluation succeeded"
        );
    }

    public static MemberSpecEvaluationInternalDTO failure(HttpStatus status, String message) {
        return new MemberSpecEvaluationInternalDTO(
                false,
                null,
                null,
                null,
                null,
                null,
                null,
                status,
                message
        );
    }
}
