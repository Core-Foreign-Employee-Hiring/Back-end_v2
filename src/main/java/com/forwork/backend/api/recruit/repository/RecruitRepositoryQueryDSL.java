package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.SalaryType;
import com.forwork.backend.api.recruit.enums.WorkDayType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecruitRepositoryQueryDSL {

    Page<Recruit> getRecruits(String keyword, List<JobCategory> jobCategories, List<WorkDayType> workDayType,
                              String workStartTime, String workEndTime, List<SalaryType> salaryType,
                              Pageable pageable);

}
