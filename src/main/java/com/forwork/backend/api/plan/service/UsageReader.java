package com.forwork.backend.api.plan.service;

import com.forwork.backend.api.plan.repository.PlanUsageLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsageReader {
    private final PlanUsageLogRepository planUsageLogRepository;

    /**
     * 사용량 조회
     */
    public Long getUsage(Long memberId, Long subscriptionId, Long featureId) {
        Long usage = planUsageLogRepository.countUsage(memberId, subscriptionId, featureId);

        return usage;
    }
}
