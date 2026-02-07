package com.forwork.backend.api.recruit.dto.response;

import com.forwork.backend.api.member.entity.JobRole;
import com.forwork.backend.api.member.entity.Visa;
import com.forwork.backend.api.recruit.entity.*;
import com.forwork.backend.api.recruit.enums.CarrerType;
import com.forwork.backend.api.recruit.enums.LanguageType;
import com.forwork.backend.api.recruit.service.RecruitUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record RecruitPreviewResponseDTO(
        @Schema(description = "공고 id")
        Long recruitId,

        @Schema(description = "공고명")
        String title,

        @Schema(description = "직무")
        List<JobRole> jobRoles,

        @Schema(description = "경력 형태 (ENUM: 신입, 경력)")
        CarrerType carrerType,

        @Schema(description = "경력 형태 직접 입력 (기타 선택 시 값 입력)")
        String directInputCarrerType,

        @Schema(description = "비자")
        List<Visa> visas,

        @Schema(description = "언어")
        List<LanguageType> languageTypes,

        @Schema(description = "근무지 주소")
        String workAddress1,

        @Schema(description = "모집 종료 기간")
        LocalDate recruitEndDate,

        @Schema(description = "회사 사진")
        String companyImageUrl,

        @Schema(description = "회사명")
        String companyName
) {

    public static RecruitPreviewResponseDTO fromEntity(Recruit recruit) {

        Set<RecruitJobRole> recruitJobRoles = recruit.getRecruitJobRoles();
        List<JobRole> jobRoles = JobRole.convertToJobRolesByRecruit(recruitJobRoles);

        Set<RecruitVisa> recruitVisas = recruit.getRecruitVisas();
        List<Visa> visas = RecruitUtils.convertToVisas(recruitVisas);

        Set<RecruitLanguageType> recruitLanguageTypes = recruit.getRecruitLanguageTypes();
        List<LanguageType> languageTypes = RecruitUtils.convertToLanguageTypes(recruitLanguageTypes);

        return new RecruitPreviewResponseDTO(
                recruit.getId(),
                recruit.getTitle(),
                jobRoles,
                recruit.getCarrerType(),
                recruit.getDirectInputCarrerType(),
                visas,
                languageTypes,
                recruit.getWorkAddress1(),
                recruit.getRecruitEndDate(),
                recruit.getCompanyImageUrl(),
                recruit.getCompanyName()
        );
    }
}
