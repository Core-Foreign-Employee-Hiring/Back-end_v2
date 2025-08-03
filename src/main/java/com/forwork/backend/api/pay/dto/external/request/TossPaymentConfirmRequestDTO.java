package com.forwork.backend.api.pay.dto.external.request;

public record TossPaymentConfirmRequestDTO(
        String paymentKey,
        String orderId,
        Long amount
) {
}
