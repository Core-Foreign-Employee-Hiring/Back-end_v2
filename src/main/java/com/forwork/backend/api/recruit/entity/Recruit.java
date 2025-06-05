package com.forwork.backend.api.recruit.entity;

import com.forwork.backend.api.member.entity.Employer;
import com.forwork.backend.api.recruit.dto.request.RecruitUpdateRequestDTO;
import com.forwork.backend.api.recruit.enums.*;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class Recruit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "recruit_id")
    private Long id;

    private String title;  // 공고 제목

    private boolean isAlwaysRecruiting;  //
    private LocalDate recruitStartDate;       // 모집 시작일
    private LocalDate recruitEndDate;         // 모집 종료일

    @Enumerated(STRING)
    private ContractType contractType;         // 계약 형태 (ENUM: 정규직, 계약직, 프리랜서 등)
    private String directInputContractType;    // 계약 형태 직접 입력 (기타일 경우)

    @Enumerated(STRING)
    private WorkType workType;                 // 근무 형태 (대면, 비대면, 혼합 등)
    private String directInputWorkType;        // 근무 형태 직접 입력

    @Enumerated(STRING)
    private WorkDayType workDayType;           // 근무 요일 (ENUM: 평일, 주말, 주 7일 등)
    private String directInputWorkDayType;     // 근무 요일 직접 입력

    private Integer workStartTime;             // 근무 시작 시간 (예: 900 → 09:00, 1330 → 13:30)
    private Integer workEndTime;               // 근무 종료 시간 (같은 방식)
    private String directInputWorkTime;        // 근무 시간 직접 입력 (텍스트)

    @Enumerated(STRING)
    private SalaryType salaryType;             // 급여 형태 (ENUM: 연봉, 월급, 일급, 시급 등)
    private Integer salary;                    // 급여 금액 (salaryType 기준 단위로 저장)
    private String directInputSalaryType;      // 급여 형태 직접 입력

    private String posterImageUrl;             // 채용 포스터 이미지 URL
    private String mainTasks;                  // 주요 업무 내용
    private String qualifications;             // 자격 요건
    private String preferences;                // 우대 사항
    private String others;                     // 기타 사항

    @Enumerated(STRING)
    private ApplicationMethod applicationMethod; // 지원 방법
    private String directInputApplicationMethod; // 링크

    @Enumerated(STRING)
    private RecruitPublishStatus recruitPublishStatus;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="employer_id")
    private Employer employer;

    @Builder.Default
    @OneToMany(mappedBy = "recruit", fetch = FetchType.LAZY)
    private List<RecruitJobCategory> recruitJobCategories = new ArrayList<>();


    public String getFormattedWorkStartTime() {
        return formatTime(workStartTime);
    }

    public String getFormattedWorkEndTime() {
        return formatTime(workEndTime);
    }

    private String formatTime(Integer time) {
        if (time == null) return null;
        int hour = time / 100;
        int minute = time % 100;
        return String.format("%02d:%02d", hour, minute);
    }

    public static Integer parseTimeStringToInt(String timeStr) {
        if (timeStr == null || !timeStr.matches("\\d{2}:\\d{2}")) {
            return 0;
        }

        try {
            String[] parts = timeStr.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                return 0;
            }

            return hour * 100 + minute;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void updateFields(RecruitUpdateRequestDTO dto) {
        this.title = dto.title();
        this.isAlwaysRecruiting = dto.isAlwaysRecruiting();
        this.recruitStartDate = dto.recruitStartDate();
        this.recruitEndDate = dto.recruitEndDate();
        this.contractType = dto.contractType();
        this.directInputContractType = dto.directInputContractType();
        this.workType = dto.workType();
        this.directInputWorkType = dto.directInputWorkType();
        this.workDayType = dto.workDayType();
        this.directInputWorkDayType = dto.directInputWorkDayType();
        this.workStartTime = parseTimeStringToInt(dto.workStartTime());
        this.workEndTime = parseTimeStringToInt(dto.workEndTime());
        this.directInputWorkTime = dto.directInputWorkTime();
        this.salaryType = dto.salaryType();
        this.salary = dto.salary();
        this.directInputSalaryType = dto.directInputSalaryType();
        this.posterImageUrl = dto.posterImageUrl();
        this.mainTasks = dto.mainTasks();
        this.qualifications = dto.qualifications();
        this.preferences = dto.preferences();
        this.others = dto.others();
        this.applicationMethod = dto.applicationMethod();
        this.directInputApplicationMethod = dto.directInputApplicationMethod();
    }


}

