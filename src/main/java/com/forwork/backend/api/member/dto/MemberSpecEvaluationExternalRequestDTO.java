package com.forwork.backend.api.member.dto;

import java.util.List;

public record MemberSpecEvaluationExternalRequestDTO(
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
        public static Education of(MemberSpecificationDTO.Education dto) {
            if (dto == null) return null;
            return new Education(
                    dto.schoolName(),
                    dto.majors(),
                    dto.earnedScore(),
                    dto.maxScore()
            );
        }
    }

    public record LanguageSkill(
            Integer klptScore,
            List<EnglishSkill> englishSkills
    ) {
        public record EnglishSkill(
                String type,
                String score
        ) {
        }

        public static LanguageSkill of(MemberSpecificationDTO.LanguageSkill dto) {
            if (dto == null) return null;

            List<LanguageSkill.EnglishSkill> englishSkills = dto.englishSkills().stream()
                    .map(es -> new LanguageSkill.EnglishSkill(es.type(), es.score()))
                    .toList();

            return new LanguageSkill(
                    dto.klptScore(),
                    englishSkills
            );
        }
    }

    public record Certification(
            String certificationName,
            Integer acquiredYear,
            Integer acquiredMonth,
            String documentUrl
    ) {
        public static Certification of(MemberSpecificationDTO.Certification dto) {
            if (dto == null) return null;

            return new Certification(
                    dto.certificationName(),
                    dto.acquiredYear(),
                    dto.acquiredMonth(),
                    dto.documentUrl()
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
            String contractType,
            String highlight
    ) {
        public static Career of(MemberSpecificationDTO.Career dto) {
            if (dto == null) return null;

            return new Career(
                    dto.companyName(),
                    dto.position(),
                    dto.startYear(),
                    dto.startMonth(),
                    dto.endYear(),
                    dto.endMonth(),
                    dto.contractType()!=null?dto.contractType().name():null,
                    dto.highlight()
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
        public static Award of(MemberSpecificationDTO.Award dto) {
            if (dto == null) return null;

            return new Award(
                    dto.awardName(),
                    dto.host(),
                    dto.acquiredYear(),
                    dto.acquiredMonth(),
                    dto.description(),
                    dto.documentUrl()
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
        public static Experience of(MemberSpecificationDTO.Experience dto) {
            if (dto == null) return null;

            return new Experience(
                    dto.experience(),
                    dto.beforeImprovementRate(),
                    dto.afterImprovementRate(),
                    dto.description(),
                    dto.insight()
            );
        }
    }

    public static MemberSpecEvaluationExternalRequestDTO of(MemberSpecificationDTO dto) {
        return new MemberSpecEvaluationExternalRequestDTO(
               Education.of(dto.education()),
               LanguageSkill.of(dto.languageSkill()),
                dto.certifications().stream()
                        .map(Certification::of)
                        .toList(),
                dto.careers().stream()
                        .map(Career::of)
                        .toList(),
                dto.awards().stream()
                        .map(Award::of)
                        .toList(),
                dto.experiences().stream()
                        .map(Experience::of)
                        .toList()
        );
    }
}
