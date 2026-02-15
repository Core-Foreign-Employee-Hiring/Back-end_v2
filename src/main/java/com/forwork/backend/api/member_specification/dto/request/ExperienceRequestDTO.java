package com.forwork.backend.api.member_specification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record ExperienceRequestDTO(
        @Schema(description = "경험")
        @Valid
        List<Experience> experiences
) {

    public record Experience(
            @Schema(description = "경험")
            @NotBlank
            String experience,

            @Schema(description = "개선률(이전)")
            Double beforeImprovementRate,

            @Schema(description = "개선률(이후)")
            Double afterImprovementRate,

            @Schema(description = "경험설명")
            @NotBlank
            String description,

            @Schema(
                    description = "시작일",
                    format = "yyyy-MM"
            )
            @NotBlank
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "yyyy-MM 형식이어야 합니다.")
            String startDate,

            @Schema(
                    description = "종료일(null -> 진행 중)",
                    format = "yyyy-MM"
            )
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "yyyy-MM 형식이어야 합니다.")
            String endDate
    ) {
    }
}
