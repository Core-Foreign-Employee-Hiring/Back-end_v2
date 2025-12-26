package com.forwork.backend.api.member_specification.dto.request;

import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.common.validation.ValidContractType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record MemberSpecificationRequestDTO(
        @Schema(description = "학력")
        @NotNull
        @Valid
        Education education,

        @Schema(description = "어학")
        @Valid
        List<LanguageSkill> languageSkills,

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

        public record LanguageSkill(
                @Schema(description = "제목")
                @NotBlank
                String title,

                @Schema(description = "점수")
                @NotBlank
                String score
        ) {
        }

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

        public record Career(

                @Schema(description = "회사명")
                @NotBlank
                String companyName,

                @Schema(description = "포지션")
                @NotBlank
                String position,

                @Schema(
                        description = "근무 시작일",
                        format = "yyyy-MM"
                )
                @NotBlank
                @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "yyyy-MM 형식이어야 합니다.")
                String startDate,

                @Schema(
                        description = "근무 종료일(null -> 재직 중)",
                        format = "yyyy-MM"
                )
                @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "yyyy-MM 형식이어야 합니다.")
                String endDate,

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

