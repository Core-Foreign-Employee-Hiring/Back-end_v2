package com.forwork.backend.api.member_specification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SpecificationEvaluationRequestDTO(
        @Schema(description = "스펙 이름")
        @NotBlank(message = "스펙 이름은 필수 입력값입니다.")
        String specName
) {
}
