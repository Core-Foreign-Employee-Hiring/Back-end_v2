package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.member.entity.Nationality;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.api.recruit.enums.LanguageType;
import com.forwork.backend.api.recruit.enums.WorkRegion;
import com.forwork.backend.api.member.entity.JobRole;
import com.forwork.backend.api.member.entity.Visa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface RecruitRepositoryQueryDSL {

    Page<Recruit> getRecruits(String keyword, Pageable pageable,
                              Set<JobRole> jobRoles, Nationality nationality, Set<LanguageType> languageTypes, Visa visa, Set<WorkRegion> workRegions, ContractType contractType);

}
