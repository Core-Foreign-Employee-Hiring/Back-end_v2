package com.forwork.backend.api.plan.repository;

import com.forwork.backend.api.plan.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select s from Subscription s" +
            " join fetch s.planVersion pv" +
            " join fetch pv.plan" +
            " where s.member.id=:memberId and s.subscriptionStatus='ACTIVE'")
    Optional<Subscription> findActiveSubscriptionByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query("update Subscription s" +
            " set s.subscriptionStatus=:toStatus" +
            " where s.member.id=:memberId and s.subscriptionStatus=:fromStatus")
    long updateStatus(@Param("memberId") Long memberId, @Param("fromStatus") String fromStatus, @Param("toStatus") String toStatus);

}
