package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.LanguageSkillSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageSkillSnapshotRepository extends JpaRepository<LanguageSkillSnapshot, Long> {
}
