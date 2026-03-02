package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.EducationSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationSnapshotRepository extends JpaRepository<EducationSnapshot, Long> {
}
