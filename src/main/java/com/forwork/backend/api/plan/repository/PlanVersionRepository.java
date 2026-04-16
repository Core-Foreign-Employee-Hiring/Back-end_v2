package com.forwork.backend.api.plan.repository;

import com.forwork.backend.api.plan.entity.PlanVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanVersionRepository extends JpaRepository<PlanVersion, Long> {
    @Query("select pv from PlanVersion pv" +
            " where pv.active=true and pv.plan.planType='FREE'")
    Optional<PlanVersion> findFreePlanVersion();

    @Query("select pv from PlanVersion pv" +
            " where pv.active=true and pv.plan.planType=:planType")
    Optional<PlanVersion> findPlanVersion(@Param("planType") String planType);


    @Query("select pv from PlanVersion pv" +
            " join fetch pv.plan" +
            " join fetch pv.item i" +
            " where i.id in :itemIds" +
            " order by i.id desc")
    List<PlanVersion> findByItemIdsWithPlan(@Param("itemIds") List<Long> itemIds);
}
