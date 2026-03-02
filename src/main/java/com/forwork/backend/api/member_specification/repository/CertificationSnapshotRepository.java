package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.CertificationSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationSnapshotRepository extends JpaRepository<CertificationSnapshot, Long> {
}
