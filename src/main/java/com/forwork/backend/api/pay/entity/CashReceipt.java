package com.forwork.backend.api.pay.entity;

import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class CashReceipt extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cash_receipt_id")
    private Long id;

    private String receiptKey;                // 현금영수증 키
    private String issueNumber;               // 발급번호
    private String issueStatus;               // 발급 상태 (IN_PROGRESS; COMPLETED; FAILED)
    private Integer amount;                   // 처리 금액
    private Integer taxFreeAmount;            // 면세 금액
    private String merchantOrderId;                   // 주문번호
    private String orderName;                 // 상품명
    private String type;                      // 소득공제 / 지출증빙
    private String transactionType;           // CONFIRM / CANCEL
    private String businessNumber;            // 사업자등록번호
    private String customerIdentityNumber;    // 소비자 식별값
    private OffsetDateTime requestedAt;               // 요청 시간 (ISO 8601)
    private String receiptUrl;                 // 영수증 URL

    private String provider;

}
