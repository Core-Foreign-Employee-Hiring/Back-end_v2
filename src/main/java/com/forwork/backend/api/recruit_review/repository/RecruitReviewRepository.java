package com.forwork.backend.api.recruit_review.repository;

import com.forwork.backend.api.recruit_review.entity.RecruitReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RecruitReviewRepository extends JpaRepository<RecruitReview, Long>, RecruitReviewRepositoryQueryDSL {


    @Query("select r from RecruitReview r" +
            " join fetch r.writer" +
            " where r.id=:recruitReviewId and r.isDeleted=false")
    Optional<RecruitReview> findByIdWithWriter(@Param("recruitReviewId")Long recruitReviewId);


    @Query("select count(*) from RecruitReview")
    Long fineToTalCount();


    @Modifying
    @Transactional
    @Query("update RecruitReview r set r.readCount=r.readCount+1 where r.id=:recruitReviewId")
    void incrementReadCount(@Param("recruitReviewId")Long recruitReviewId);


    @Modifying
    @Transactional
    @Query("update RecruitReview r set r.readCount=:newReadCount where r.id=:recruitReviewId")
    void updateViewCount(@Param("recruitReviewId")Long recruitReviewId, @Param("newReadCount")Integer newReadCount);
}
