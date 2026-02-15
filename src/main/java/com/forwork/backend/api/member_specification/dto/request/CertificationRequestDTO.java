package com.forwork.backend.api.member_specification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record CertificationRequestDTO(
        @Schema(description = "자격증")
        @Valid
        List<Certification> certifications

) {
    public record Certification(

            @Schema(description = "자격증 이름")
            @NotBlank
            String certificationName,

            @Schema(
                    description = "취득날짜",
                    format = "yyyy-MM"
            )
            @NotBlank
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "yyyy-MM 형식이어야 합니다.")
            String acquiredDate,

            @Schema(description = "증빙자료")
            String documentUrl
    ) {
    }
}
