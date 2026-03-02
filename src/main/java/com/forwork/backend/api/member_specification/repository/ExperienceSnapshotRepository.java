package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.ExperienceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceSnapshotRepository extends JpaRepository<ExperienceSnapshot, Long> {
}
