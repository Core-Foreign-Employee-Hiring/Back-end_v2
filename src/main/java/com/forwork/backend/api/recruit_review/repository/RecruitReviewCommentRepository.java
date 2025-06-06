package com.forwork.backend.api.recruit_review.repository;

import com.forwork.backend.api.recruit_review.dto.query.RecruitReviewChildCommentStatQueryDTO;
import com.forwork.backend.api.recruit_review.entity.RecruitReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecruitReviewCommentRepository extends JpaRepository<RecruitReviewComment, Long> {

    @Query("select c from RecruitReviewComment c" +
            " where c.id=:id and c.parent is null")
    Optional<RecruitReviewComment> findParentCommentIfRoot(@Param("id") Long id);

    @Query("select c from RecruitReviewComment c" +
            " left join fetch c.writer" +
            " left join fetch c.parent" +
            " where c.id=:commentId and c.isDeleted=false")
    Optional<RecruitReviewComment> findByIdWithWriterAndParent(@Param("commentId") Long commentId);


    @Query("select count(*) from RecruitReviewComment c" +
            " where c.recruitReview.id=:recruitReviewId")
    Long findCommentCount(@Param("recruitReviewId") Long recruitReviewId);

    @Query("select c from RecruitReviewComment c " +
            "join fetch c.writer " +
            "left join fetch c.parent " +
            "where c.recruitReview.id=:recruitReviewId and c.isDeleted=false " +
            "order by c.id")
    List<RecruitReviewComment> findRecruitReviewCommentsByRecruitReviewId(@Param("recruitReviewId") Long recruitReviewId);


    @Query("select new com.forwork.backend.api.recruit_review.dto.query.RecruitReviewChildCommentStatQueryDTO(" +
            "COUNT(c), " +
            "SUM(case when c.isDeleted = true then 1 else 0 end)) " +
            "from RecruitReviewComment c " +
            "where c.parent.id = :parentId")
    RecruitReviewChildCommentStatQueryDTO findChildCommentStatsByParentId(@Param("parentId") Long parentId);

}
