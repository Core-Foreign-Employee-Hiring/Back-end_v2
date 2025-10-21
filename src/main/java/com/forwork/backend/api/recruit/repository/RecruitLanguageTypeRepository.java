package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.recruit.entity.RecruitLanguageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RecruitLanguageTypeRepository extends JpaRepository<RecruitLanguageType, Long> {
    @Modifying @Transactional
    @Query("delete from RecruitLanguageType rlt where rlt.recruit.id=:recruitId")
    void deleteByRecruitId(@Param("recruitId") Long recruitId);
}
