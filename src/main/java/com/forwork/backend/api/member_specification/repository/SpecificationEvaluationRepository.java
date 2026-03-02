package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.dto.projection.SpecificationEvaluationRankProjection;
import com.forwork.backend.api.member_specification.entity.SpecificationEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SpecificationEvaluationRepository extends JpaRepository<SpecificationEvaluation, Long> {

    @Query("select se from SpecificationEvaluation se" +
            " left join se.memberSpecification" +
            " where se.id=:specEvaluationId")
    Optional<SpecificationEvaluation> findBySpecificationEvaluationIdWithSpec(@Param("specEvaluationId") Long specEvaluationId);


    @Query("select se from SpecificationEvaluation se" +
            " join fetch se.memberSpecification ms" +
            " join fetch ms.member" +
            " where se.id=:specEvaluationId")
    Optional<SpecificationEvaluation> findBySpecificationEvaluationIdWithMember(@Param("specEvaluationId") Long specEvaluationId);


    @Query("select se from SpecificationEvaluation se" +
            " join se.memberSpecification ms" +
            " join ms.member m" +
            " where m.id=:memberId" +
            " order by se.id desc")
    Page<SpecificationEvaluation> findSpecByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select count(se) from SpecificationEvaluation se" +
            " where se.score >= :score")
    long countHigherThan(@Param("score") Integer score);

    @Query("select se.score as score, coalesce(count(se1), 0) as count from SpecificationEvaluation se" +
            " left join SpecificationEvaluation se1 on se1.score >= se.score" +
            " where se.score in :scores" +
            " group by se.score")
    List<SpecificationEvaluationRankProjection> countHigherThan(@Param("scores") List<Integer> scores);

    @Modifying
    @Transactional
    @Query("delete from SpecificationEvaluation se where se.memberSpecification.id=:memberSpecificationId")
    void deleteByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);

}
