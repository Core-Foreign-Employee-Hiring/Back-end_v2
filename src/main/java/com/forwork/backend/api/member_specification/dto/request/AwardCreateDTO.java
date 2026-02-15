package com.forwork.backend.api.member_specification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record AwardCreateDTO(
        @Schema(description = "수상")
        @Valid
        List<Award> awards
) {
    public record Award(
            @Schema(description = "수상명")
            @NotBlank
            String awardName,

            @Schema(description = "주최")
            @NotBlank
            String host,

            @Schema(
                    description = "취득날짜",
                    format = "yyyy-MM"
            )
            @NotBlank
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "yyyy-MM 형식이어야 합니다.")
            String acquiredDate,

            @Schema(description = "설명")
            String description,

            @Schema(description = "증빙자료")
            String documentUrl
    ) {
    }
}
