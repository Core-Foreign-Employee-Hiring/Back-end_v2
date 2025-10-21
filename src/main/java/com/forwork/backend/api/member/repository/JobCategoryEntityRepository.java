package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.JobCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobCategoryEntityRepository extends JpaRepository<JobCategoryEntity, Long> {
    @Query("select j from JobCategoryEntity j " +
            "where j.jobCategory in :jobCategories")
    List<JobCategoryEntity> findAllByJobCategories(@Param("jobCategories") List<String> jobCategories);

}
