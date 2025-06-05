package com.forwork.backend.api.recruit.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record RecruitDraftResponseDTO(
        @Schema(description = "공고 제목")
        String title,

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
        String directInputApplicationMethod
) {

        public static RecruitDraftResponseDTO fromEntity(Recruit recruit, List<JobCategory> jobCategories) {

                return new RecruitDraftResponseDTO(
                        recruit.getTitle(),
                        recruit.isAlwaysRecruiting(),
                        recruit.getRecruitStartDate(),
                        recruit.getRecruitEndDate(),

                        recruit.getContractType(),
                        recruit.getDirectInputContractType(),

                        jobCategories,

                        recruit.getWorkType(),
                        recruit.getDirectInputWorkType(),

                        recruit.getWorkDayType(),
                        recruit.getDirectInputWorkDayType(),

                        recruit.getFormattedWorkEndTime(),
                        recruit.getFormattedWorkEndTime(),
                        recruit.getDirectInputWorkTime(),

                        recruit.getSalaryType(),
                        recruit.getSalary(),
                        recruit.getDirectInputSalaryType(),

                        recruit.getPosterImageUrl(),
                        recruit.getMainTasks(),
                        recruit.getQualifications(),
                        recruit.getPreferences(),
                        recruit.getOthers(),

                        recruit.getApplicationMethod(),
                        recruit.getDirectInputApplicationMethod()
                );
        }

}
