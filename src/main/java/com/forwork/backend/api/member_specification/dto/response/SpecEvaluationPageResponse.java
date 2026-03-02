package com.forwork.backend.api.member_specification.dto.response;

import com.forwork.backend.api.member_specification.entity.SpecificationEvaluation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record SpecEvaluationPageResponse(
        @Schema(description = "스펙 평가 id")
        Long specEvaluationId,

        @Schema(description = "스펙 이름")
        String specName,

        @Schema(description = "진단일")
        LocalDate evaluatedDate,

        @Schema(description = "상위 몇 퍼?")
        Double topPercent,

        @Schema(description = "요약")
        String summary
) {

    public static SpecEvaluationPageResponse of(SpecificationEvaluation specEvaluation, Double topPercent) {
        return new SpecEvaluationPageResponse(
                specEvaluation.getId(),
                specEvaluation.getSpecName(),
                specEvaluation.getEvaluatedDate(),
                topPercent,
                createSummary(specEvaluation.getAnalysis())
        );
    }

    private static String createSummary(String analysis) {
        if (analysis == null) {
            return null;
        }

        // 첫 구분선 이전까지만
        String firstSection = analysis.split("---")[0];

        // 줄바꿈만 제거
        return firstSection
                .replace("\r\n", " ")
                .replace("\n", " ")
                .replace("\r", " ")
                .trim();
    }


}