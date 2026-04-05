package com.forwork.backend.api.plan.repository;

import com.forwork.backend.api.plan.entity.PlanUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanUsageLogRepository extends JpaRepository<PlanUsageLog, Long> {

    @Query("select count(p) from PlanUsageLog p" +
            " where p.member.id = :memberId and p.subscription.id = :subscriptionId and p.feature.id = :featureId")
    Long countUsage(@Param("memberId") Long memberId, @Param("subscriptionId") Long subscriptionId, @Param("featureId") Long featureId);
}
