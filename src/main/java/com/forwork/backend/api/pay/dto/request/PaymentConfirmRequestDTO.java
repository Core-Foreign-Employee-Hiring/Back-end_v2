package com.forwork.backend.api.pay.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentConfirmRequestDTO(
        @Schema(description = "토스가 넘겨주는 paymentKey")
        String paymentKey,
        @Schema(description = "주문 ID")
        String merchantOrderId,
        @Schema(description = "결제 금액")
        String amount
) {
}
