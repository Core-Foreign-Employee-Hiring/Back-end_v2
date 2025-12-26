package com.forwork.backend.api.member_specification.dto.internal;

import com.forwork.backend.api.member_specification.entity.*;
import com.forwork.backend.api.recruit.enums.ContractType;

import java.util.List;

public record MemberSpecificationDTO(
        Long memberSpecificationId,
        Education education,
        List<LanguageSkill> languageSkills,
        List<Certification> certifications,
        List<Career> careers,
        List<Award> awards,
        List<Experience> experiences
) {


    public record Education(
            String schoolName,
            List<String> majors,
            String admissionDate,  // yyyy-MM
            String graduationDate, // yyyy-MM
            Double earnedScore,
            Double maxScore
    ) {

        public static Education of(MemberEducation education, List<String> majors) {


            return new Education(
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
            String title,
            String score
    ) {

        public static LanguageSkill of(MemberLanguageSkill memberLanguageSkill) {
            return new LanguageSkill(
                    memberLanguageSkill.getTitle(),
                    memberLanguageSkill.getScore()
            );
        }
    }

    public record Certification(
            String certificationName,
            String acquiredDate,
            String documentUrl
    ) {
        public static Certification of(MemberCertification entity) {
            return new Certification(
                    entity.getCertificationName(),
                    entity.getAcquiredDate(),
                    entity.getDocumentUrl()
            );
        }

    }

    public record Career(
            String companyName,
            String position,
            String startDate,  // yyyy-MM
            String endDate,    // yyyy-MM
            ContractType contractType,
            String highlight
    ) {

        public static Career of(MemberCareer entity) {
            return new Career(
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
            String awardName,
            String host,
            String acquiredDate,
            String description,
            String documentUrl
    ) {
        public static Award of(MemberAward entity) {
            return new Award(
                    entity.getAwardName(),
                    entity.getHost(),
                    entity.getAcquiredDate(),
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
            String startDate,
            String endDate
    ) {
        public static Experience of(MemberExperience entity) {
            return new Experience(
                    entity.getExperience(),
                    entity.getBeforeImprovementRate(),
                    entity.getAfterImprovementRate(),
                    entity.getDescription(),
                    entity.getStartDate(),
                    entity.getEndDate()
            );
        }
    }

    public static MemberSpecificationDTO of(
            Long memberSpecificationId,
            Education education,
            List<LanguageSkill> languageSkills,
            List<Certification> certifications,
            List<Career> careers,
            List<Award> awards,
            List<Experience> experiences
    ) {
        return new MemberSpecificationDTO(
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
