package com.forwork.backend.api.plan.repository;

import com.forwork.backend.api.plan.entity.PlanVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlanVersionRepository extends JpaRepository<PlanVersion, Long> {
    @Query("select pv from PlanVersion pv" +
            " where pv.active=true and pv.plan.planType='FREE'")
    Optional<PlanVersion> findFreePlanVersion();
}
