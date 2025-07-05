package com.forwork.backend.api.recruit.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forwork.backend.api.member.entity.Address;
import com.forwork.backend.api.member.entity.CompanyType;
import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record RecruitRequestDTO(
        @Schema(description = "공고 제목")
        String title,

        @Schema(description = "로고 사진")
        String companyImageUrl,
        @Schema(description = "회사 이름")
        String companyName,
        @Schema(description = "우편번호")
        String zipcode,
        @Schema(description = "주소")
        String address1,
        @Schema(description = "상세 주소")
        String address2,
        @Schema(description = "기업 형태")
        CompanyType companyType,
        @Schema(description = "대표자명")
        String representativeName,
        @Schema(description = "설립일")
        LocalDate establishedDate,
        @Schema(description = "업종")
        String businessType,

        @JsonProperty("isAlwaysRecruiting")
        @Schema(description = "상시 모집 기간 여부")
        boolean isAlwaysRecruiting,
        @Schema(description = "모집 시작일")
        LocalDate recruitStartDate,
        @Schema(description = "모집 종료일")
        LocalDate recruitEndDate,

        @Schema(description = "계약 형태 (ENUM: 정규직, 계약직, 프리랜서 등)")
        ContractType contractType,
        @Schema(description = "계약 형태 직접 입력 (기타 선택 시 값 입력)")
        String directInputContractType,

        @Schema(description = "직종")
        List<JobCategory> jobCategories,

        @Schema(description = "근무 형태 (ENUM: 대면, 비대면, 혼합 등)")
        WorkType workType,
        @Schema(description = "근무 형태 직접 입력")
        String directInputWorkType,

        @Schema(description = "근무 요일 (ENUM: 평일, 주말, 주 6일 등)")
        WorkDayType workDayType,
        @Schema(description = "근무 요일 직접 입력")
        String directInputWorkDayType,

        @Schema(description = "근무 시작 시간 (형식: HH:mm, 예: 09:30")
        String workStartTime,
        @Schema(description = "근무 종료 시간 (형식: HH:mm, 예: 18:00")
        String workEndTime,
        @Schema(description = "근무 시간 직접 입력")
        String directInputWorkTime,

        @Schema(description = "급여 형태 (ENUM: 연봉, 월급, 시급 등)")
        SalaryType salaryType,
        @Schema(description = "급여 금액 (단위는 salaryType에 따라 해석)")
        Integer salary,
        @Schema(description = "급여 형태 직접 입력")
        String directInputSalaryType,

        @Schema(description = "채용 포스터 이미지 URL (선택 입력)")
        String posterImageUrl,
        @Schema(description = "주요 업무 내용")
        String mainTasks,
        @Schema(description = "자격 요건")
        String qualifications,
        @Schema(description = "우대 사항")
        String preferences,
        @Schema(description = "기타 사항")
        String others,

        @Schema(description = "지원 방법")
        ApplicationMethod applicationMethod,
        @Schema(description = "링크")
        String directInputApplicationMethod,

        @Schema(description = "PUBLISHED: 최종 등록, DRAFT: 임시 저장 ")
        RecruitPublishStatus recruitPublishStatus
) {
    public Recruit toEntity() {
        return Recruit.builder()
                .title(title)
                .companyImageUrl(companyImageUrl)
                .companyName(companyName)
                .companyAddress(new Address(zipcode, address1, address2))
                .companyType(companyType)
                .representativeName(representativeName)
                .establishedDate(establishedDate)
                .businessType(businessType)
                .isAlwaysRecruiting(isAlwaysRecruiting)
                .recruitStartDate(recruitStartDate)
                .recruitEndDate(recruitEndDate)
                .contractType(contractType)
                .directInputContractType(directInputContractType)
                .workType(workType)
                .directInputWorkType(directInputWorkType)
                .workDayType(workDayType)
                .directInputWorkDayType(directInputWorkDayType)
                .workStartTime(Recruit.parseTimeStringToInt(workStartTime) )
                .workEndTime(Recruit.parseTimeStringToInt(workEndTime))
                .directInputWorkTime(directInputWorkTime)
                .salaryType(salaryType)
                .salary(salary)
                .directInputSalaryType(directInputSalaryType)
                .posterImageUrl(posterImageUrl)
                .mainTasks(mainTasks)
                .qualifications(qualifications)
                .preferences(preferences)
                .others(others)
                .applicationMethod(applicationMethod)
                .directInputApplicationMethod(directInputApplicationMethod)
                .recruitPublishStatus(recruitPublishStatus)
                .build();
    }

}


