package com.forwork.backend.api.plan.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.plan.entity.PlanVersion;
import com.forwork.backend.api.plan.entity.Subscription;
import com.forwork.backend.api.plan.enums.SubscriptionType;
import com.forwork.backend.api.plan.repository.PlanVersionRepository;
import com.forwork.backend.api.plan.repository.SubscriptionRepository;
import com.forwork.backend.common.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static com.forwork.backend.common.response.ErrorStatus.INTERNAL_SERVER_EXCEPTION;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionCreator {
    private final SubscriptionRepository subscriptionRepository;
    private final PlanVersionRepository planVersionRepository;


    /**
     * 회원가입 시 구독 생성
     */
    public Long createInitialSubscription(Member member) {
        PlanVersion freePlanVersion = planVersionRepository.findFreePlanVersion()
                .orElseThrow(() -> {
                    log.warn("[createInitialSubscription][Free Plan 없음.]");
                    return new InternalServerException(INTERNAL_SERVER_EXCEPTION.getMessage());
                });


        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Seoul"));

        Subscription subscription = Subscription.builder()
                .subscriptionType(SubscriptionType.ACTIVE.getValue())
                .startDate(now.toLocalDate())
                .endDate(null)
                .member(member)
                .planVersion(freePlanVersion)
                .build();

        Long id = subscriptionRepository.save(subscription).getId();

        return id;
    }

}
