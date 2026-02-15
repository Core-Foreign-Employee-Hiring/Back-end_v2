package com.forwork.backend.api.member_specification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

public record EducationRequestDTO(
        @Schema(description = "학교")
        @NotBlank
        String schoolName,

        @Schema(description = "전공")
        @NotNull
        @Size(min = 1)
        List<String> majors,

        @Schema(
                description = "입학일",
                format = "yyyy-MM"
        )
        @NotBlank
        @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "yyyy-MM 형식이어야 합니다.")
        String admissionDate,

        @Schema(
                description = "졸업일(null -> 재학 중)",
                format = "yyyy-MM"
        )
        @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "yyyy-MM 형식이어야 합니다.")
        String graduationDate,

        @Schema(description = "내 학점")
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "4.5")
        Double earnedScore,

        @Schema(description = "총점")
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "4.5")
        Double maxScore
) {
}
