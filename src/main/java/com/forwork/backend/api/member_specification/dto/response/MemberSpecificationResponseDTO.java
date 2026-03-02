package com.forwork.backend.api.member_specification.dto.response;

import com.forwork.backend.api.member_specification.dto.internal.MemberSpecificationDTO;
import com.forwork.backend.api.member_specification.dto.internal.SpecificationSnapshotDTO;
import com.forwork.backend.api.recruit.enums.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MemberSpecificationResponseDTO(
        @Schema(description = "학력")
        Education education,

        @Schema(description = "어학")
        List<LanguageSkill> languageSkills,

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
            @Schema(description = "학력 id")
            Long educationId,

            @Schema(description = "학교")
            String schoolName,

            @Schema(description = "전공")
            List<String> majors,

            @Schema(
                    description = "입학일",
                    format = "yyyy-MM"
            )
            String admissionDate,

            @Schema(
                    description = "졸업일(null -> 재학 중)",
                    format = "yyyy-MM"
            )
            String graduationDate,

            @Schema(description = "내 학점")
            Double earnedScore,

            @Schema(description = "총점")
            Double maxScore
    ) {

        public static Education of(MemberSpecificationDTO.Education dto) {
            if (dto == null) return null;
            return new Education(
                    dto.educationId(),
                    dto.schoolName(),
                    dto.majors(),
                    dto.admissionDate(),
                    dto.graduationDate(),
                    dto.earnedScore(),
                    dto.maxScore()
            );
        }

        public static Education of(SpecificationSnapshotDTO.Education dto) {
            if (dto == null) return null;
            return new Education(
                    dto.educationId(),
                    dto.schoolName(),
                    dto.majors(),
                    dto.admissionDate(),
                    dto.graduationDate(),
                    dto.earnedScore(),
                    dto.maxScore()
            );
        }


    }

    public record LanguageSkill(
            @Schema(description = "어학 id")
            Long languageSkillId,

            @Schema(description = "제목")
            String title,

            @Schema(description = "점수")
            String score
    ) {

        public static LanguageSkill of(MemberSpecificationDTO.LanguageSkill memberLanguageSkill) {
            return new LanguageSkill(
                    memberLanguageSkill.languageSkillId(),
                    memberLanguageSkill.title(),
                    memberLanguageSkill.score()
            );
        }

        public static LanguageSkill of(SpecificationSnapshotDTO.LanguageSkill memberLanguageSkill) {
            return new LanguageSkill(
                    memberLanguageSkill.languageSkillId(),
                    memberLanguageSkill.title(),
                    memberLanguageSkill.score()
            );
        }

    }

    public record Certification(
            @Schema(description = "certificationId")
            Long certificationId,

            @Schema(description = "자격증 이름")
            String certificationName,

            @Schema(
                    description = "취득날짜",
                    format = "yyyy-MM"
            )
            String acquiredDate,

            @Schema(description = "증빙자료")
            String documentUrl
    ) {

        public static Certification of(MemberSpecificationDTO.Certification dto) {
            if (dto == null) return null;

            return new Certification(
                    dto.certificationId(),
                    dto.certificationName(),
                    dto.acquiredDate(),
                    dto.documentUrl()
            );
        }

        public static Certification of(SpecificationSnapshotDTO.Certification dto) {
            if (dto == null) return null;

            return new Certification(
                    dto.certificationId(),
                    dto.certificationName(),
                    dto.acquiredDate(),
                    dto.documentUrl()
            );
        }

    }

    public record Career(
            @Schema(description = "경력 id")
            Long careerId,

            @Schema(description = "회사명")
            String companyName,

            @Schema(description = "포지션")
            String position,

            @Schema(
                    description = "근무 시작일",
                    format = "yyyy-MM"
            )
            String startDate,

            @Schema(
                    description = "근무 종료일(null -> 재학 중)",
                    format = "yyyy-MM"
            )
            String endDate,

            @Schema(description = "계약형태")
            ContractType contractType,

            @Schema(description = "어필 경험")
            String highlight
    ) {

        public static Career of(MemberSpecificationDTO.Career dto) {
            if (dto == null) return null;

            return new Career(
                    dto.careerId(),
                    dto.companyName(),
                    dto.position(),
                    dto.startDate(),
                    dto.endDate(),
                    dto.contractType(),
                    dto.highlight()
            );
        }

        public static Career of(SpecificationSnapshotDTO.Career dto) {
            if (dto == null) return null;

            return new Career(
                    dto.careerId(),
                    dto.companyName(),
                    dto.position(),
                    dto.startDate(),
                    dto.endDate(),
                    dto.contractType(),
                    dto.highlight()
            );
        }
    }

    public record Award(
            @Schema(description = "수상 id")
            Long awardId,

            @Schema(description = "수상명")
            String awardName,

            @Schema(description = "주최")
            String host,

            @Schema(description = "취득날짜")
            String acquiredDate,


            @Schema(description = "설명")
            String description,

            @Schema(description = "증빙자료")
            String documentUrl
    ) {

        public static Award of(MemberSpecificationDTO.Award dto) {
            if (dto == null) return null;

            return new Award(
                    dto.awardId(),
                    dto.awardName(),
                    dto.host(),
                    dto.acquiredDate(),
                    dto.description(),
                    dto.documentUrl()
            );
        }

        public static Award of(SpecificationSnapshotDTO.Award dto) {
            if (dto == null) return null;

            return new Award(
                    dto.awardId(),
                    dto.awardName(),
                    dto.host(),
                    dto.acquiredDate(),
                    dto.description(),
                    dto.documentUrl()
            );
        }
    }

    public record Experience(
            @Schema(description = "경험 id")
            Long experienceId,

            @Schema(description = "경험")
            String experience,

            @Schema(description = "개선률(이전)")
            Double beforeImprovementRate,

            @Schema(description = "개선률(이후)")
            Double afterImprovementRate,

            @Schema(description = "경험설명")
            String description,

            @Schema(
                    description = "시작일",
                    format = "yyyy-MM"
            )
            String startDate,

            @Schema(
                    description = "종료일(null -> 진행 중)",
                    format = "yyyy-MM"
            )
            String endDate
    ) {

        public static Experience of(MemberSpecificationDTO.Experience dto) {
            if (dto == null) return null;

            return new Experience(
                    dto.experienceId(),
                    dto.experience(),
                    dto.beforeImprovementRate(),
                    dto.afterImprovementRate(),
                    dto.description(),
                    dto.startDate(),
                    dto.endDate()
            );
        }

        public static Experience of(SpecificationSnapshotDTO.Experience dto) {
            if (dto == null) return null;

            return new Experience(
                    dto.experienceId(),
                    dto.experience(),
                    dto.beforeImprovementRate(),
                    dto.afterImprovementRate(),
                    dto.description(),
                    dto.startDate(),
                    dto.endDate()
            );
        }
    }


    public static MemberSpecificationResponseDTO of(MemberSpecificationDTO dto) {
        return new MemberSpecificationResponseDTO(

                Education.of(dto.education()),
                dto.languageSkills().stream()
                        .map(LanguageSkill::of)
                        .toList(),
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

    public static MemberSpecificationResponseDTO of(SpecificationSnapshotDTO dto) {
        return new MemberSpecificationResponseDTO(

                Education.of(dto.education()),
                dto.languageSkills().stream()
                        .map(LanguageSkill::of)
                        .toList(),
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
