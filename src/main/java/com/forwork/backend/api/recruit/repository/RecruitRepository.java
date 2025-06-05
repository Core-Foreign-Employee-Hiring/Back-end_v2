package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitRepositoryQueryDSL {

    @Query("select r from Recruit r" +
            " left join fetch r.employer" +
            " left join fetch r.recruitJobCategories rjc" +
            " left join fetch rjc.jobCategoryEntity " +
            " where r.id=:recruitId and r.recruitPublishStatus='PUBLISHED'")
    Optional<Recruit> findByRecruitId(@Param("recruitId")Long recruitId);


    @Query("select r from Recruit r" +
            " left join fetch r.employer e" +
            " left join fetch r.recruitJobCategories rjc" +
            " left join fetch rjc.jobCategoryEntity" +
            " where e.id=:employerId and r.recruitPublishStatus='DRAFT'" +
            " order by r.id desc" +
            " limit 1")
    Optional<Recruit> getLatestDraft(@Param("employerId") Long employerId);
}
