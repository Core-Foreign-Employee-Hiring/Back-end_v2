package com.forwork.backend.api.pay.dto.external.response;

import com.forwork.backend.api.pay.entity.CashReceipt;
import com.forwork.backend.api.pay.enums.CashReceiptProvider;

import java.time.OffsetDateTime;

public record TossCashReceiptResponse(
        String receiptKey,                // 현금영수증 키
        String issueNumber,               // 발급번호
        String issueStatus,               // 발급 상태 (IN_PROGRESS, COMPLETED, FAILED)
        Integer amount,                   // 처리 금액
        Integer taxFreeAmount,            // 면세 금액
        String orderId,                   // 주문번호
        String orderName,                 // 상품명
        String type,                      // 소득공제 / 지출증빙
        String transactionType,           // CONFIRM / CANCEL
        String businessNumber,            // 사업자등록번호
        String customerIdentityNumber,    // 소비자 식별값
        Failure failure,                  // 실패 정보 (nullable)
        String requestedAt,               // 요청 시간 (ISO 8601)
        String receiptUrl                 // 영수증 URL
) {

    public record Failure(
            String code,      // 에러 코드
            String message    // 에러 메시지
    ) {
    }


    public CashReceipt toEntity() {

        OffsetDateTime parsedRequestedAt = null;

        if (this.requestedAt != null) {
            parsedRequestedAt = OffsetDateTime.parse(this.requestedAt);
        }

        return CashReceipt.builder()
                .receiptKey(receiptKey)
                .issueNumber(issueNumber)
                .issueStatus(issueStatus)
                .amount(amount)
                .taxFreeAmount(taxFreeAmount)
                .merchantOrderId(orderId)
                .orderName(orderName)
                .type(type)
                .transactionType(transactionType)
                .businessNumber(businessNumber)
                .customerIdentityNumber(customerIdentityNumber)
                .requestedAt(parsedRequestedAt)
                .receiptUrl(receiptUrl)
                .provider(CashReceiptProvider.TOSS.getValue())
                .build();
    }

}

