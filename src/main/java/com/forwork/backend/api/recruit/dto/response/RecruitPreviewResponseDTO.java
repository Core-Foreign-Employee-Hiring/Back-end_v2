package com.forwork.backend.api.recruit.dto.response;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.entity.RecruitJobCategory;
import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.api.recruit.enums.SalaryType;
import com.forwork.backend.api.recruit.service.RecruitUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record RecruitPreviewResponseDTO(
        @Schema(description = "공고 id")
        Long recruitId,
        @Schema(description = "회사 사진")
        String companyImageUrl,
        @Schema(description = "회사명")
        String companyName,
        @Schema(description = "모집 종료 기간")
        LocalDate recruitEndDate,
        @Schema(description = "공고명")
        String title,
        @Schema(description = "직무")
        List<JobCategory> jobCategories,
        @Schema(description = "급여 종류")
        SalaryType salaryType,
        @Schema(description = "급여")
        Integer salary,
        @Schema(description = "계약 형태 (ENUM: 정규직, 계약직, 프리랜서 등)")
        ContractType contractType,
        @Schema(description = "우편번호")
        String zipcode,
        @Schema(description = "주소")
        String address1,
        @Schema(description = "상세주소")
        String address2
) {

    public static RecruitPreviewResponseDTO fromEntity(Recruit recruit) {

        Set<RecruitJobCategory> recruitJobCategories = recruit.getRecruitJobCategories();
        List<JobCategory> jobCategories = RecruitUtils.convertToJobCategories(recruitJobCategories);

        return new RecruitPreviewResponseDTO(
                recruit.getId(),
                recruit.getCompanyImageUrl(),
                recruit.getCompanyName(),
                recruit.getRecruitEndDate(),
                recruit.getTitle(),
                jobCategories,
                recruit.getSalaryType(),
                recruit.getSalary(),
                recruit.getContractType(),
                recruit.getCompanyZipcode(),
                recruit.getCompanyAddress1(),
                recruit.getCompanyAddress2()
        );
    }
}
