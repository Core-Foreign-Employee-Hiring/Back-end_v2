package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.ExperienceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceSnapshotRepository extends JpaRepository<ExperienceSnapshot, Long> {
    @Query("select e from ExperienceSnapshot e" +
            " where e.specificationEvaluation.id=:specEvaluationId")
    List<ExperienceSnapshot> findBySpecEvaluationId(@Param("specEvaluationId") Long specEvaluationId);

}
