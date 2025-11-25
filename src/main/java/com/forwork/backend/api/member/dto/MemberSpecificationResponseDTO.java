package com.forwork.backend.api.member.dto;

import com.forwork.backend.api.member.entity.*;
import com.forwork.backend.api.recruit.enums.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MemberSpecificationResponseDTO(
        @Schema(description = "학력")
        Education education,

        @Schema(description = "어학")
        LanguageSkill languageSkill,

        @Schema(description = "자격증")
        List<Certification> certifications,

        @Schema(description = "경력사항")
        List<Career> careers,

        @Schema(description = "경력사항")
        List<Award> awards,

        @Schema(description = "경험")
        List<Experience> experiences
) {


    public record Education(
            @Schema(description = "학교")
            String schoolName,

            @Schema(description = "전공")
            List<String> majors,

            @Schema(description = "내 학점")
            Double earnedScore,

            @Schema(description = "총점")
            Double maxScore
    ) {

        public static Education of(MemberEducation education, List<String> majors) {
            return new Education(
                    education.getSchoolName(),
                    majors,
                    education.getEarnedScore(),
                    education.getMaxScore()
            );
        }

    }

    public record LanguageSkill(
            @Schema(description = "한국어 능력 시험 점수")
            Integer klptScore,

            @Schema(description = "영어 능력")
            List<LanguageSkill.EnglishSkill> englishSkills

    ) {
        public record EnglishSkill(
                @Schema(description = "시험 종류")
                String type,

                @Schema(description = "점수")
                String score
        ) {
        }

        public static LanguageSkill of(MemberLanguageSkill memberLanguageSkill, List<MemberEnglishSkill> englishSkills) {
            List<EnglishSkill> englishSkillDtos = englishSkills.stream()
                    .map(es -> new EnglishSkill(es.getType(), es.getScore()))
                    .toList();

            return new LanguageSkill(memberLanguageSkill.getKlptScore(), englishSkillDtos);
        }
    }

    public record Certification(

            @Schema(description = "자격증 이름")
            String certificationName,

            @Schema(description = "취득날짜(년)")
            Integer acquiredYear,

            @Schema(description = "취득날짜(월)")
            Integer acquiredMonth,

            @Schema(description = "증빙자료")
            String documentUrl
    ) {
        public static Certification of(MemberCertification entity) {
            return new Certification(
                    entity.getCertificationName(),
                    entity.getAcquiredYear(),
                    entity.getAcquiredMonth(),
                    entity.getDocumentUrl()
            );
        }

    }

    public record Career(

            @Schema(description = "회사명")
            String companyName,

            @Schema(description = "포지션")
            String position,

            @Schema(description = "근무 시작일(년)")
            Integer startYear,

            @Schema(description = "근무 시작일(월)")
            Integer startMonth,

            @Schema(description = "근무 종료일(년)")
            Integer endYear,

            @Schema(description = "근무 종료일(월)")
            Integer endMonth,

            @Schema(description = "계약형태")
            ContractType contractType,

            @Schema(description = "어필 경험")
            String highlight
    ) {

        public static Career of(MemberCareer entity) {
            return new Career(
                    entity.getCompanyName(),
                    entity.getPosition(),
                    entity.getStartYear(),
                    entity.getStartMonth(),
                    entity.getEndYear(),
                    entity.getEndMonth(),
                    ContractType.from(entity.getContractType()),
                    entity.getHighlight()
            );
        }
    }

    public record Award(
            @Schema(description = "수상명")
            String awardName,

            @Schema(description = "주최")
            String host,

            @Schema(description = "취득날짜(년)")
            Integer acquiredYear,

            @Schema(description = "취득날짜(월)")
            Integer acquiredMonth,

            @Schema(description = "설명")
            String description,

            @Schema(description = "증빙자료")
            String documentUrl
    ) {
        public static Award of(MemberAward entity) {
            return new Award(
                    entity.getAwardName(),
                    entity.getHost(),
                    entity.getAcquiredYear(),
                    entity.getAcquiredMonth(),
                    entity.getDescription(),
                    entity.getDocumentUrl()
            );
        }
    }

    public record Experience(
            @Schema(description = "경험")
            String experience,

            @Schema(description = "개선률(이전)")
            Double beforeImprovementRate,

            @Schema(description = "개선률(이후)")
            Double afterImprovementRate,

            @Schema(description = "경험설명")
            String description,

            @Schema(description = "인사이트")
            String insight
    ) {
        public static Experience of(MemberExperience entity) {
            return new Experience(
                    entity.getExperience(),
                    entity.getBeforeImprovementRate(),
                    entity.getAfterImprovementRate(),
                    entity.getDescription(),
                    entity.getInsight()
            );
        }
    }

    public static MemberSpecificationResponseDTO of(
            Education education,
            LanguageSkill languageSkill,
            List<Certification> certifications,
            List<Career> careers,
            List<Award> awards,
            List<Experience> experiences
    ) {
        return new MemberSpecificationResponseDTO(
                education,
                languageSkill,
                certifications,
                careers,
                awards,
                experiences
        );
    }

}
