package com.forwork.backend.api.recruit.service;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.SalaryType;
import com.forwork.backend.api.recruit.enums.WorkDayType;
import com.forwork.backend.api.recruit.repository.RecruitRepository;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.forwork.backend.common.response.ErrorStatus.RECRUIT_NOT_FOUND_EXCEPTION;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecruitReader {
    private final RecruitRepository recruitRepository;

    public Recruit getRecruit(Long recruitId) {
        Recruit recruit = recruitRepository.findByRecruitId(recruitId)
                .orElseThrow(() -> {
                    log.warn("[getRecruit][공고 없음.][recruitId= {}]", recruitId);
                    return new NotFoundException(RECRUIT_NOT_FOUND_EXCEPTION.getMessage());
                });

        return recruit;
    }

    public Recruit getLatestDraft(Long employerId) {
        Recruit recruit = recruitRepository.getLatestDraft(employerId)
                .orElseThrow(() -> {
                    log.warn("[getLatestDraft][가장 최근 임시 공고 없음.][employerId= {}]", employerId);
                    return new NotFoundException(RECRUIT_NOT_FOUND_EXCEPTION.getMessage());
                });

        return recruit;
    }


    public Page<Recruit> getRecruits(String keyword, List<JobCategory> jobCategories, List<WorkDayType> workDayType,
                                     String workStartTime, String workEndTime, List<SalaryType> salaryType,
                                     Pageable pageable) {
        Page<Recruit> recruits = recruitRepository.getRecruits(keyword, jobCategories, workDayType, workStartTime, workEndTime, salaryType, pageable);

        return recruits;
    }

}
