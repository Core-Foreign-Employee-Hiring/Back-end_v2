package com.forwork.backend.api.plan.service;

import com.forwork.backend.api.plan.entity.Subscription;
import com.forwork.backend.api.plan.repository.SubscriptionRepository;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

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


    /**
     * 하루 전 만료된 PRO 구독 조회
     */
    public List<Subscription> findSubscriptionsExpiredYesterday() {
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Seoul"));

        LocalDate endDate = now.toLocalDate().minusDays(1);

        List<Subscription> expiredActiveSubscriptions = subscriptionRepository.findExpiredActiveSubscriptions(endDate);

        return expiredActiveSubscriptions;
    }

}
