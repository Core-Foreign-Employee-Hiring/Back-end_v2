package com.forwork.backend.api.plan.repository;

import com.forwork.backend.api.plan.entity.PlanVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlanVersionRepository extends JpaRepository<PlanVersion, Long> {
    @Query("select pv from PlanVersion pv" +
            " where pv.active=true and pv.plan.planType='FREE'")
    Optional<PlanVersion> findFreePlanVersion();

    @Query("select pv from PlanVersion pv" +
            " where pv.active=true and pv.plan.planType=:planType")
    Optional<PlanVersion> findPlanVersion(@Param("planType") String planType);
}
