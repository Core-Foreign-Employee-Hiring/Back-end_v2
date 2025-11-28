package com.forwork.backend.api.member.dto;

import com.forwork.backend.api.member.entity.SpecificationEvaluation;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberSpecEvaluationResponseDTO(
        @Schema(description = "경험")
        Long specEvaluationId,
        @Schema(description = "경험")
        Integer experience,
        @Schema(description = "자격증")
        Integer certificate,
        @Schema(description = "어학")
        Integer language,
        @Schema(description = "경력")
        Integer career,
        @Schema(description = "학력")
        Integer education,
        @Schema(description = "분석?")
        String analysis
) {

    public static MemberSpecEvaluationResponseDTO of(SpecificationEvaluation specificationEvaluation) {
        return new MemberSpecEvaluationResponseDTO(
                specificationEvaluation.getId(),
                specificationEvaluation.getExperience(),
                specificationEvaluation.getCertificate(),
                specificationEvaluation.getLanguage(),
                specificationEvaluation.getCareer(),
                specificationEvaluation.getEducation(),
                specificationEvaluation.getAnalysis()
        );
    }
}
