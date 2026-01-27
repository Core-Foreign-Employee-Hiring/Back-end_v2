package com.forwork.backend.api.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeDetailResponse {
    private Long resumeId;
    private String resumeName;
    private String profileImageUrl;
    private String introduction;

    // 회원 기본 정보
    private MemberBasicInfo memberBasicInfo;

    // 선택 항목들 (true인 것만 데이터 포함)
    private List<ResumeUrlDto> urls;
    private List<EducationDto> educations;
    private List<CertificationDto> certifications;
    private List<LanguageSkillDto> languageSkills;
    private List<CareerDto> careers;
    private List<AwardDto> awards;
    private List<ExperienceDto> experiences;

    // 선택 여부
    private boolean includeIntroduction;
    private boolean includeEducation;
    private boolean includeCertificate;
    private boolean includeLanguage;
    private boolean includeCareer;
    private boolean includeAward;
    private boolean includeActivity;
    private boolean includeUrls;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberBasicInfo {
        private String name;         // 이름
        private String jobRole;      // 직무
        private String phoneNumber;  // 연락처
        private String email;        // 이메일
        private String nationality;  // 국적
        private String visa;         // 비자
        private String birthday;     // 생년월일
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResumeUrlDto {
        private Long id;
        private String urlTitle;
        private String urlLink;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EducationDto {
        private Long id;
        private String schoolName;
        private String admissionDate;
        private String graduationDate;
        private Double earnedScore;
        private Double maxScore;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CertificationDto {
        private Long id;
        private String certificationName;
        private String acquiredDate;
        private String documentUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LanguageSkillDto {
        private Long id;
        private String title;
        private String score;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CareerDto {
        private Long id;
        private String companyName;
        private String position;
        private String startDate;
        private String endDate;
        private String contractType;
        private String highlight;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AwardDto {
        private Long id;
        private String awardName;
        private String host;
        private String acquiredDate;
        private String description;
        private String documentUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExperienceDto {
        private Long id;
        private String experience;
        private Double beforeImprovementRate;
        private Double afterImprovementRate;
        private String description;
        private String startDate;
        private String endDate;
    }
}
