package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.recruit.entity.RecruitJobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitJobCategoryRepository extends JpaRepository<RecruitJobCategory, Long> {
}
