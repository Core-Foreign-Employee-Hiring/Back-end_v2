package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.CareerSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerSnapshotRepository extends JpaRepository<CareerSnapshot, Long> {
}
