package com.forwork.backend.api.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.util.List;

public record MemberSpecificationRequestDTO(
        @Schema(description = "학력")
        @NotNull
        @Valid
        Education education,

        @Schema(description = "어학")
        @NotNull
        @Valid
        LanguageSkill languageSkill,

        @Schema(description = "자격증")
        @Valid
        List<Certification> certifications

) {


    public record Education(
            @Schema(description = "학교")
            @NotBlank
            String schoolName,

            @Schema(description = "전공")
            @NotNull
            @Size(min = 1)
            List<String> majors,

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

    public record LanguageSkill(
            @Schema(description = "한국어 능력 시험 점수")
            @NotNull
            @Range(min = 1, max = 6)
            Integer klptScore,

            @Schema(description = "영어 능력")
            List<EnglishSkill> englishSkills


    ) {
        public record EnglishSkill(
                @Schema(description = "시험 종류")
                String type,

                @Schema(description = "점수")
                String score
        ) {
        }
    }

    public record Certification(

            @Schema(description = "자격증 이름")
            @NotBlank
            String certificationName,

            @Schema(description = "취득날짜(년)")
            @NotNull
            Integer acquiredYear,

            @Schema(description = "취득날짜(월)")
            @NotNull
            Integer acquiredMonth,

            @Schema(description = "증빙자료")
            String documentUrl
    ) {
    }
}

