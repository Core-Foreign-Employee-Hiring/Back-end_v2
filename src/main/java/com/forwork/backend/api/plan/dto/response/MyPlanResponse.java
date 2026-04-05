package com.forwork.backend.api.plan.dto.response;

import com.forwork.backend.api.plan.entity.Subscription;
import com.forwork.backend.api.plan.enums.PlanType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record MyPlanResponse(
        @Schema(description = "plan 종료")
        PlanType planType,
        @Schema(description = "만료날짜")
        LocalDate endDate
) {

    public static MyPlanResponse of(Subscription subscription) {
        String plan = subscription.getPlanVersion().getPlan().getPlanType();

        return new MyPlanResponse(
                PlanType.from(plan),
                subscription.getEndDate()
        );
    }
}
