package com.forwork.backend.api.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CashReceiptResponse(
        @Schema(description = "현금영수증 URL")
        String receiptUrl
) {
}
