package com.forwork.backend.api.plan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionScheduler {

    private final SubscriptionService subscriptionService;

    /**
     * 매일 00시 실행
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void downgradeExpiredSubscriptions() {
        log.info("[SubscriptionScheduler] 만료 구독 다운그레이드 시작");

        subscriptionService.downgradeExpiredSubscriptions();

        log.info("[SubscriptionScheduler] 만료 구독 다운그레이드 종료");
    }
}
