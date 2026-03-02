package com.forwork.backend.api.member_specification.dto.internal;

import com.forwork.backend.api.member_specification.entity.*;
import com.forwork.backend.api.recruit.enums.ContractType;

import java.util.List;

public record SpecificationSnapshotDTO(
        Long specEvaluationId,
        Education education,
        List<LanguageSkill> languageSkills,
        List<Certification> certifications,
        List<Career> careers,
        List<Award> awards,
        List<Experience> experiences
) {
    public record Education(
            Long educationId,
            String schoolName,
            List<String> majors,
            String admissionDate,  // yyyy-MM
            String graduationDate, // yyyy-MM
            Double earnedScore,
            Double maxScore
    ) {

        public static Education of(EducationSnapshot education, List<String> majors) {


            return new Education(
                    education.getId(),
                    education.getSchoolName(),
                    majors,
                    education.getAdmissionDate(),
                    education.getGraduationDate(),
                    education.getEarnedScore(),
                    education.getMaxScore()
            );
        }

    }

    public record LanguageSkill(
            Long languageSkillId,
            String title,
            String score
    ) {

        public static LanguageSkill of(LanguageSkillSnapshot memberLanguageSkill) {
            return new LanguageSkill(
                    memberLanguageSkill.getId(),
                    memberLanguageSkill.getTitle(),
                    memberLanguageSkill.getScore()
            );
        }
    }

    public record Certification(
            Long certificationId,
            String certificationName,
            String acquiredDate,
            String documentUrl
    ) {
        public static Certification of(CertificationSnapshot entity) {
            return new Certification(
                    entity.getId(),
                    entity.getCertificationName(),
                    entity.getAcquiredDate(),
                    entity.getDocumentUrl()
            );
        }

    }

    public record Career(
            Long careerId,
            String companyName,
            String position,
            String startDate,  // yyyy-MM
            String endDate,    // yyyy-MM
            ContractType contractType,
            String highlight
    ) {

        public static Career of(CareerSnapshot entity) {
            return new Career(
                    entity.getId(),
                    entity.getCompanyName(),
                    entity.getPosition(),
                    entity.getStartDate(),
                    entity.getEndDate(),
                    ContractType.from(entity.getContractType()),
                    entity.getHighlight()
            );
        }
    }

    public record Award(
            Long awardId,
            String awardName,
            String host,
            String acquiredDate,
            String description,
            String documentUrl
    ) {
        public static Award of(AwardSnapshot entity) {
            return new Award(
                    entity.getId(),
                    entity.getAwardName(),
                    entity.getHost(),
                    entity.getAcquiredDate(),
                    entity.getDescription(),
                    entity.getDocumentUrl()
            );
        }
    }

    public record Experience(
            Long experienceId,
            String experience,
            Double beforeImprovementRate,
            Double afterImprovementRate,
            String description,
            String startDate,
            String endDate
    ) {
        public static Experience of(ExperienceSnapshot entity) {
            return new Experience(
                    entity.getId(),
                    entity.getExperience(),
                    entity.getBeforeImprovementRate(),
                    entity.getAfterImprovementRate(),
                    entity.getDescription(),
                    entity.getStartDate(),
                    entity.getEndDate()
            );
        }
    }

    public static SpecificationSnapshotDTO of(
            Long memberSpecificationId,
            Education education,
            List<LanguageSkill> languageSkills,
            List<Certification> certifications,
            List<Career> careers,
            List<Award> awards,
            List<Experience> experiences
    ) {
        return new SpecificationSnapshotDTO(
                memberSpecificationId,
                education,
                languageSkills,
                certifications,
                careers,
                awards,
                experiences
        );
    }
}
