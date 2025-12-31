package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.SpecificationEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SpecificationEvaluationRepository extends JpaRepository<SpecificationEvaluation, Long> {

    @Query("select se from SpecificationEvaluation se" +
            " left join se.memberSpecification" +
            " where se.id=:specEvaluationId")
    Optional<SpecificationEvaluation> findBySpecificationEvaluationIdWithSpec(@Param("specEvaluationId") Long specEvaluationId);

    @Query("select count(se) from SpecificationEvaluation se" +
            " where se.score > :score")
    long countHigherThan(@Param("score") Integer score);

    @Modifying @Transactional
    @Query("delete from SpecificationEvaluation se where se.memberSpecification.id=:memberSpecificationId")
    void deleteByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}
