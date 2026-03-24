package com.forwork.backend.api.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CashReceiptIssueRequest(
        @Schema(description = "현금영수증 종류")
        @NotBlank
        String type,

        @Schema(description = "소비자 인증수단")
        @NotBlank
        String customerIdentityNumber
) {
}
