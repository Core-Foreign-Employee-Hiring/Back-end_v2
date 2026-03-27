package com.forwork.backend.api.pay.dto.response;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pay.entity.Payment;
import com.forwork.backend.api.pay.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record PaymentHistoryResponse(
        @Schema(description = "아카이브 id")
        Long passArchiveId,
        @Schema(description = "썸네일")
        String thumbnailUrl,
        @Schema(description = "콘텐츠명")
        String title,
        @Schema(description = "주문번호")
        String merchantOrderId,
        @Schema(description = "결제일시")
        OffsetDateTime approvedAt,
        @Schema(description = "결제상태")
        PaymentStatus paymentStatus,
        @Schema(description = "가격")
        String totalAmount,
        @Schema(description = "다운로드 유무")
        boolean downloaded
) {

    public static PaymentHistoryResponse of(Payment payment, PassArchive archive, boolean downloaded) {
        return new PaymentHistoryResponse(
                archive.getPassArchiveId(),
                archive.getThumbnail().getFileUrl(),
                archive.getTitle(),
                payment.getOrder().getMerchantOrderId(),
                payment.getApprovedAt(),
                payment.getPaymentStatus(),
                payment.getTotalAmount(),
                downloaded
        );
    }

}
