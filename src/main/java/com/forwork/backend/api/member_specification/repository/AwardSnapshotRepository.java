package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.AwardSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AwardSnapshotRepository extends JpaRepository<AwardSnapshot, Long> {
    @Query("select a from AwardSnapshot a" +
            " where a.specificationEvaluation.id=:specEvaluationId")
    List<AwardSnapshot> findBySpecEvaluationId(@Param("specEvaluationId") Long specEvaluationId);

}
