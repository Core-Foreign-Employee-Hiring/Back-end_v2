package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.CertificationSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CertificationSnapshotRepository extends JpaRepository<CertificationSnapshot, Long> {
    @Query("select l from CertificationSnapshot l" +
            " where l.specificationEvaluation.id=:specEvaluationId")
    List<CertificationSnapshot> findBySpecEvaluationId(@Param("specEvaluationId") Long specEvaluationId);
}
