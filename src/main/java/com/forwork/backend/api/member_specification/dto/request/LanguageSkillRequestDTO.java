package com.forwork.backend.api.member_specification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record LanguageSkillRequestDTO(
        @Schema(description = "어학")
        @Valid
        List<LanguageSkill> languageSkills


) {
    public record LanguageSkill(
            @Schema(description = "제목")
            @NotBlank
            String title,

            @Schema(description = "점수")
            @NotBlank
            String score
    ) {
    }

}
