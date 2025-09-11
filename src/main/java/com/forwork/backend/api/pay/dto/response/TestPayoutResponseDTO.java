package com.forwork.backend.api.pay.dto.response;

import com.forwork.backend.api.pay.entity.Payout;
import com.forwork.backend.api.pay.enums.PayoutStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record TestPayoutResponseDTO(
        @Schema(description = "payoutId")
        Long payoutId,
        @Schema(description = "금액")
        String totalAmount,
        @Schema(description = "상태")
        PayoutStatus payoutStatus,
        @Schema(description = "판매자 id")
        Long sellerId

) {

    public static TestPayoutResponseDTO of(Payout payout) {
        return new TestPayoutResponseDTO(
                payout.getId(),
                payout.getTotalAmount(),
                payout.getPayoutStatus(),
                payout.getSeller().getId()
        );
    }
}
