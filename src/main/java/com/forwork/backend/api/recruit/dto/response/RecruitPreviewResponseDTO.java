package com.forwork.backend.api.recruit.dto.response;

import com.forwork.backend.api.member.entity.Employer;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.SalaryType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record RecruitPreviewResponseDTO(
        @Schema(description = "공고 id")
        Long recruitId,

        @Schema(description = "공고 제목")
        String title,
        @Schema(description = "회사명")
        String companyName,
        @Schema(description = "우편번호")
        String zipcode,
        @Schema(description = "주소")
        String address1,
        @Schema(description = "상세주소")
        String address2,

        @Schema(description = "근무 시작 시간")
        String workStartTime,
        @Schema(description = "근무 종료 시간")
        String workEndTime,

        @Schema(description = "급여 종류")
        SalaryType salaryType,
        @Schema(description = "급여")
        Integer salary,

        @Schema(description = "모집 종료 기간")
        LocalDate recruitEndDate
) {

    public static RecruitPreviewResponseDTO fromEntity(Recruit recruit) {
        Employer employer = recruit.getEmployer();

        String formattedWorkStartTime = recruit.getFormattedWorkStartTime();
        String formattedWorkEndTime = recruit.getFormattedWorkEndTime();

        return new RecruitPreviewResponseDTO(
                recruit.getId(),
                recruit.getTitle(),
                employer != null ? employer.getCompanyName() : null,
                employer != null ? employer.getAddress().getZipcode() : null,
                employer != null ? employer.getAddress().getAddress1() : null,
                employer != null ? employer.getAddress().getAddress2() : null,
                formattedWorkStartTime,
                formattedWorkEndTime,
                recruit.getSalaryType(),
                recruit.getSalary(),
                recruit.getRecruitEndDate()
        );
    }
}
