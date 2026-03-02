package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.LanguageSkillSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LanguageSkillSnapshotRepository extends JpaRepository<LanguageSkillSnapshot, Long> {

    @Query("select l from LanguageSkillSnapshot l" +
            " where l.specificationEvaluation.id=:specEvaluationId")
    List<LanguageSkillSnapshot> findBySpecEvaluationId(@Param("specEvaluationId") Long specEvaluationId);
}
