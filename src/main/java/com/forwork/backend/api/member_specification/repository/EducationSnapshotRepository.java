package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.EducationSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EducationSnapshotRepository extends JpaRepository<EducationSnapshot, Long> {

    @Query("select e from EducationSnapshot e" +
            " where e.specificationEvaluation.id=:specEvaluationId")
    Optional<EducationSnapshot> findBySpecEvaluationId(@Param("specEvaluationId") Long specEvaluationId);
}
