package com.forwork.backend.api.pay.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;

public record PaymentConfirmRequestDTO(
        @Schema(description = "토스가 넘겨주는 paymentKey")
        String paymentKey,

        @Schema(description = "주문 ID")
        String merchantOrderId,

        @Schema(description = "결제 금액")
        String amount,

        @Schema(description = "결제 이용약관")
        @AssertTrue(message = "결제 이용약관에 동의해야 합니다.")
        boolean agreePaymentTerms,

        @Schema(description = "개인정보 수집")
        @AssertTrue(message = "개인정보 수집에 동의해야 합니다.")
        boolean agreePrivacyPolicy,

        @Schema(description = "결제 취소 및 환불/규정")
        @AssertTrue(message = "결제 취소 및 환불/교환 규정에 동의해야 합니다.")
        boolean agreeRefundPolicy
) {
}
