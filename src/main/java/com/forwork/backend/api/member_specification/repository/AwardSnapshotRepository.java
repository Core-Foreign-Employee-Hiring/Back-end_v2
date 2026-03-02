package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.AwardSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardSnapshotRepository extends JpaRepository<AwardSnapshot, Long> {
}
