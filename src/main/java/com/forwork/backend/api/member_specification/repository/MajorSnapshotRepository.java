package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MajorSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorSnapshotRepository extends JpaRepository<MajorSnapshot, Long> {
}
