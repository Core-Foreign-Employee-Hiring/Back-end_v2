package com.forwork.backend.api.pay.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PayoutRequestDTO(
        @Schema(description="인출할 결제 내역 ids")
        List<Long> paymentIds
) {
}