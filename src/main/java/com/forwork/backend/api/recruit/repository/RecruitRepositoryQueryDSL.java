package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.ContractType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecruitRepositoryQueryDSL {

    Page<Recruit> getRecruits(String keyword, List<JobCategory> jobCategories, List<ContractType> contractTypes, Pageable pageable);

}
