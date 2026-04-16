package com.forwork.backend.api.plan.service;


import com.forwork.backend.api.plan.entity.PlanVersion;
import com.forwork.backend.api.plan.enums.PlanType;
import com.forwork.backend.api.plan.repository.PlanVersionRepository;
import com.forwork.backend.common.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.forwork.backend.common.response.ErrorStatus.INTERNAL_SERVER_EXCEPTION;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlanReader {
    private final PlanVersionRepository planVersionRepository;

    public PlanVersion getFreePlan() {
        return getPlan(PlanType.FREE);
    }

    private PlanVersion getPlan(PlanType planType) {
        PlanVersion planVersion = planVersionRepository.findPlanVersion(planType.getValue())
                .orElseThrow(() -> {
                    log.warn("[getPlan][{} Plan 없음.]", planType);
                    return new InternalServerException(INTERNAL_SERVER_EXCEPTION.getMessage());
                });

        return planVersion;
    }

    public List<PlanVersion> getPlansByItemIds(List<Long> itemIds) {
        return planVersionRepository.findByItemIdsWithPlan(itemIds);
    }
}
