package com.forwork.backend.api.item.dto.response;

import com.forwork.backend.api.plan.entity.PlanVersion;
import com.forwork.backend.api.plan.enums.PlanType;
import io.swagger.v3.oas.annotations.media.Schema;

public record PlanResponse(
        @Schema(description = "item id")
        Long itemId,
        @Schema(description = "플랜 종류")
        PlanType planType

)
        implements ItemResponse {


    public static PlanResponse of(PlanVersion plan) {
        return new PlanResponse(
                plan.getItem().getId(),
                PlanType.from(plan.getPlan().getPlanType())
        );
    }
}
