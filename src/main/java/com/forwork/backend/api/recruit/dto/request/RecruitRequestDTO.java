package com.forwork.backend.api.recruit.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forwork.backend.api.member.entity.*;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record RecruitRequestDTO(
        @Schema(description = "공고 제목")
        String title,

        @Schema(description = "로고 사진")
        String companyImageUrl,
        @Schema(description = "회사 이름")
        String companyName,
        @Schema(description = "회사 우편번호")
        String companyZipcode,
        @Schema(description = "회사 주소")
        String companyAddress1,
        @Schema(description = "회사 상세 주소")
        String companyAddress2,
        @Schema(description = "회사 위도")
        Double companyLatitude,
        @Schema(description = "회사 경도")
        Double companyLongitude,
        @Schema(description = "근무지 우편번호")
        String workZipcode,
        @Schema(description = "근무지 주소")
        String workAddress1,
        @Schema(description = "근무지 상세 주소")
        String workAddress2,
        @Schema(description = "근무지 위도")
        Double workLatitude,
        @Schema(description = "근무지 경도")
        Double workLongitude,
        @Schema(description = "기업 형태")
        CompanyType companyType,
        @Schema(description = "대표자명")
        String representativeName,
        @Schema(description = "설립일")
        LocalDate establishedDate,
        @Schema(description = "업종")
        String businessType,
        @Schema(description = "직무")
        @Size(max=5, message="최대 5개")
        Set<JobRole> jobRoles,
        @Schema(description = "언어")
        @Size(max=5, message="최대 5개")
        Set<LanguageType> languageTypes,
        @Schema(description = "비자")
        Set<Visa> visas,
        @JsonProperty("isAlwaysRecruiting")
        @Schema(description = "상시 모집 기간 여부")
        boolean isAlwaysRecruiting,
        @Schema(description = "모집 시작일")
        LocalDate recruitStartDate,
        @Schema(description = "모집 종료일")
        LocalDate recruitEndDate,

        @Schema(description = "모집 기간 직접 입력")
        String directInputRecruitDate,

        @Schema(description = "계약 형태 (ENUM: 정규직, 인턴, 계약직, 기타)")
        ContractType contractType,
        @Schema(description = "계약 형태 직접 입력 (기타 선택 시 값 입력)")
        String directInputContractType,

        @Schema(description = "경력 형태 (ENUM: 신입, 경력)")
        CarrerType carrerType,
        @Schema(description = "경력 형태 직접 입력 (기타 선택 시 값 입력)")
        String directInputCarrerType,

        @Schema(description = "직종")
        List<JobCategory> jobCategories,

        @Schema(description = "근무 형태 (ENUM: 대면, 비대면, 혼합 등)")
        WorkType workType,
        @Schema(description = "근무 형태 직접 입력")
        String directInputWorkType,

        @Schema(description = "근무 요일 (ENUM: 평일, 주말, 주 6일 등)")
        WorkDayPatternType workDayPatternType,
        @Schema(description = "근무 요일 직접 입력")
        Set<WorkingDays> workingDays,
        @Schema(description = "근무 요일 기타사항")
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

        @Schema(description = "웹사이트 링크")
        String websiteUrl,

        @Schema(description = "회사소개")
        String companyIntroduction,

        @Schema(description = "제출서류")
        Set<SubmissionDocumentType> submissionDocuments,

        @Schema(description = "제출서류 직접입력")
        String directInputSubmissionDocument,

        @Schema(description = "PUBLISHED: 최종 등록, DRAFT: 임시 저장 ")
        RecruitPublishStatus recruitPublishStatus
) {
    public Recruit toEntity() {
        return Recruit.builder()
                .title(title)
                .companyImageUrl(companyImageUrl)
                .companyName(companyName)
                .companyZipcode(companyZipcode)
                .companyAddress1(companyAddress1)
                .companyAddress2(companyAddress2)
                .companyLatitude(companyLatitude)
                .companyLongitude(companyLongitude)
                .workZipcode(workZipcode)
                .workAddress1(workAddress1)
                .workAddress2(workAddress2)
                .workLatitude(workLatitude)
                .workLongitude(workLongitude)
                .companyType(companyType)
                .representativeName(representativeName)
                .establishedDate(establishedDate)
                .businessType(businessType)
                .isAlwaysRecruiting(isAlwaysRecruiting)
                .recruitStartDate(recruitStartDate)
                .recruitEndDate(recruitEndDate)
                .directInputRecruitDate(directInputRecruitDate)
                .contractType(contractType)
                .directInputContractType(directInputContractType)
                .carrerType(carrerType)
                .directInputCarrerType(directInputCarrerType)
                .workType(workType)
                .directInputWorkType(directInputWorkType)
                .workDayPatternType(workDayPatternType)
                .workingDays(WorkingDays.toBit(workingDays))
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
                .workRegion(WorkRegion.fromPrefix(workAddress1).getDbValue())
                .websiteUrl(websiteUrl)
                .companyIntroduction(companyIntroduction)
                .submissionDocumentBits(SubmissionDocumentType.toBit(submissionDocuments))
                .directInputSubmissionDocument(directInputSubmissionDocument)
                .build();
    }

}


