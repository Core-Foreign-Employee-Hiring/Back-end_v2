package com.forwork.backend.api.pay.event;

public record PaymentCompletedEvent(
        Long memberId,
        String merchantOrderId
) {
}
