package com.forwork.backend.api.member.dto;

import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.common.validation.ValidContractType;
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
        List<Certification> certifications,

        @Schema(description = "경력사항")
        @Valid
        List<Career> careers,

        @Schema(description = "경력사항")
        @Valid
        List<Award> awards,

        @Schema(description = "경험")
        @Valid
        List<Experience> experiences
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

        public record Career(

                @Schema(description = "회사명")
                @NotBlank
                String companyName,

                @Schema(description = "포지션")
                @NotBlank
                String position,

                @Schema(description = "근무 시작일(년)")
                @NotNull
                Integer startYear,

                @Schema(description = "근무 시작일(월)")
                @NotNull
                Integer startMonth,

                @Schema(description = "근무 종료일(년)")
                @NotNull
                Integer endYear,

                @Schema(description = "근무 종료일(월)")
                @NotNull
                Integer endMonth,

                @Schema(description = "계약형태")
                @ValidContractType(anyOf = {ContractType.CONTRACT, ContractType.REGULAR, ContractType.INTERN})
                ContractType contractType,

                @Schema(description = "어필 경험")
                String highlight

        ) {
        }

        public record Award(
                @Schema(description = "수상명")
                @NotBlank
                String awardName,

                @Schema(description = "주최")
                @NotBlank
                String host,

                @Schema(description = "취득날짜(년)")
                @NotNull
                Integer acquiredYear,

                @Schema(description = "취득날짜(월)")
                @NotNull
                Integer acquiredMonth,

                @Schema(description = "설명")
                String description,

                @Schema(description = "증빙자료")
                String documentUrl
        ) {
        }

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

                @Schema(description = "인사이트")
                String insight
        ) {
        }
}

