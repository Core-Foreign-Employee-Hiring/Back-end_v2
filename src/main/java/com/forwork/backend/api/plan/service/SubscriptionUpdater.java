package com.forwork.backend.api.plan.service;

import com.forwork.backend.api.plan.enums.SubscriptionStatus;
import com.forwork.backend.api.plan.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionUpdater {
    private final SubscriptionRepository subscriptionRepository;


    /**
     * ACTIVE -> REPLACED
     */

    public long replaceActiveSubscription(Long memberId) {
        return updateStatus(memberId, SubscriptionStatus.ACTIVE, SubscriptionStatus.REPLACED);
    }


    private long updateStatus(Long memberId, SubscriptionStatus from, SubscriptionStatus to) {
        return subscriptionRepository.updateStatus(memberId, from.getValue(), to.getValue());
    }

    /**
     * 해당 구독들 비활성화
     */
    public long processExpiration(List<Long> subscriptionIds) {
        return updateStatus(subscriptionIds, SubscriptionStatus.EXPIRED);
    }

    private long updateStatus(List<Long> subscriptionIds, SubscriptionStatus toStatus) {
        return subscriptionRepository.updateStatus(subscriptionIds, toStatus.getValue());
    }
}
