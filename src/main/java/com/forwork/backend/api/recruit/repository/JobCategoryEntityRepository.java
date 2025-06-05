package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.JobCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobCategoryEntityRepository extends JpaRepository<JobCategoryEntity, Long> {
    @Query("select j from JobCategoryEntity j " +
            "where j.jobCategory in :jobCategories")
    List<JobCategoryEntity> findAllByJobCategories(@Param("jobCategories") List<JobCategory> jobCategories);

}
