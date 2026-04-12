package com.forwork.backend.api.plan.service;

import com.forwork.backend.api.plan.enums.SubscriptionStatus;
import com.forwork.backend.api.plan.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
}
