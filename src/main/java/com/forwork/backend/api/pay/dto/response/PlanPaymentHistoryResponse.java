package com.forwork.backend.api.pay.dto.response;

import com.forwork.backend.api.pay.entity.Payment;
import com.forwork.backend.api.plan.enums.PlanType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record PlanPaymentHistoryResponse(
        @Schema(description = "주문 id")
        String merchantOrderId,

        @Schema(description = "구독제")
        PlanType planType,

        @Schema(description = "결제 날짜")
        OffsetDateTime approvedAt,

        @Schema(description = "결제 금액")
        String totalAmount
) implements PaymentHistoryResponse {


    public static PlanPaymentHistoryResponse of(Payment payment) {
        return new PlanPaymentHistoryResponse(
                payment.getOrder().getMerchantOrderId(),
                PlanType.PRO,
                payment.getApprovedAt(),
                payment.getTotalAmount()
        );
    }
}
