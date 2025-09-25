package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.recruit.entity.RecruitJobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RecruitJobCategoryRepository extends JpaRepository<RecruitJobCategory, Long> {
    @Modifying @Transactional
    @Query("delete from RecruitJobCategory rjc where rjc.recruit.id=:recruitId")
    void deleteByRecruitId(@Param("recruitId") Long recruitId);
}
