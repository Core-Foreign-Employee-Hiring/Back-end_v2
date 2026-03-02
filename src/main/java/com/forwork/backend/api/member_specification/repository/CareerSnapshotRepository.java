package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.CareerSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CareerSnapshotRepository extends JpaRepository<CareerSnapshot, Long> {
    @Query("select c from CareerSnapshot c" +
            " where c.specificationEvaluation.id=:specEvaluationId")
    List<CareerSnapshot> findBySpecEvaluationId(@Param("specEvaluationId") Long specEvaluationId);
}
