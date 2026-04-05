package com.forwork.backend.api.plan.service;

import com.forwork.backend.api.plan.entity.Subscription;
import com.forwork.backend.api.plan.repository.SubscriptionRepository;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.forwork.backend.common.response.ErrorStatus.PLAN_NOT_FOUND_EXCEPTION;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionReader {
    private final SubscriptionRepository subscriptionRepository;


    /**
     * 유저의 현재 구독 조회
     */
    public Subscription getActiveSubscription(Long memberId) {
        Subscription subscription = subscriptionRepository.findActiveSubscriptionByMemberId(memberId)
                .orElseThrow(() -> {
                    log.warn("[getActiveSubscription][Plan 없음.][memberId= {}]", memberId);
                    return new NotFoundException(PLAN_NOT_FOUND_EXCEPTION.getMessage());
                });


        return subscription;
    }
}
