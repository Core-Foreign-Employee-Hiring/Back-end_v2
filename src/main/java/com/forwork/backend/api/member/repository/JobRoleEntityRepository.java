package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.JobRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRoleEntityRepository extends JpaRepository<JobRoleEntity, Long> {
    @Query("select j from JobRoleEntity j " +
            "where j.jobRole in :jobRoles")
    List<JobRoleEntity> findAllByJobRoles(@Param("jobRoles") List<String> jobRoles);
}
