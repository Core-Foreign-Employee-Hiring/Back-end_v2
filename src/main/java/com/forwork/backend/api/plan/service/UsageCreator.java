package com.forwork.backend.api.plan.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.plan.entity.Feature;
import com.forwork.backend.api.plan.entity.PlanUsageLog;
import com.forwork.backend.api.plan.entity.Subscription;
import com.forwork.backend.api.plan.repository.PlanUsageLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsageCreator {
    private final PlanUsageLogRepository planUsageLogRepository;

    /**
     * 사용량 기록
     */
    public void logUsage(Member member, Subscription subscription, Feature feature) {

        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Seoul"));

        PlanUsageLog log = PlanUsageLog.builder()
                .member(member)
                .subscription(subscription)
                .feature(feature)
                .usedAt(now)
                .usedDate(now.toLocalDate())
                .build();

        planUsageLogRepository.save(log);
    }
}
