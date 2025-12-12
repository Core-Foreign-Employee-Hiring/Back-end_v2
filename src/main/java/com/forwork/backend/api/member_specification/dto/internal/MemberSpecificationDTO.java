package com.forwork.backend.api.member_specification.dto.internal;

import com.forwork.backend.api.member_specification.entity.*;
import com.forwork.backend.api.recruit.enums.ContractType;

import java.util.List;

public record MemberSpecificationDTO(
        Long memberSpecificationId,
        Education education,
        LanguageSkill languageSkill,
        List<Certification> certifications,
        List<Career> careers,
        List<Award> awards,
        List<Experience> experiences
) {


    public record Education(
            String schoolName,
            List<String> majors,
            Double earnedScore,
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
            Integer klptScore,
            List<LanguageSkill.EnglishSkill> englishSkills

    ) {
        public record EnglishSkill(
                String type,
                String score
        ) {
        }

        public static LanguageSkill of(MemberLanguageSkill memberLanguageSkill, List<MemberEnglishSkill> englishSkills) {
            List<LanguageSkill.EnglishSkill> englishSkillDtos = englishSkills.stream()
                    .map(es -> new LanguageSkill.EnglishSkill(es.getType(), es.getScore()))
                    .toList();

            return new LanguageSkill(memberLanguageSkill.getKlptScore(), englishSkillDtos);
        }
    }

    public record Certification(
            String certificationName,
            Integer acquiredYear,
            Integer acquiredMonth,
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
            String companyName,
            String position,
            Integer startYear,
            Integer startMonth,
            Integer endYear,
            Integer endMonth,
            ContractType contractType,
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
            String awardName,
            String host,
            Integer acquiredYear,
            Integer acquiredMonth,
            String description,
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
            String experience,
            Double beforeImprovementRate,
            Double afterImprovementRate,
            String description,
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

    public static MemberSpecificationDTO of(
            Long memberSpecificationId,
            Education education,
            LanguageSkill languageSkill,
            List<Certification> certifications,
            List<Career> careers,
            List<Award> awards,
            List<Experience> experiences
    ) {
        return new MemberSpecificationDTO(
                memberSpecificationId,
                education,
                languageSkill,
                certifications,
                careers,
                awards,
                experiences
        );
    }
}
