package com.forwork.backend.api.pay.enums;

public enum PayoutStatus {
    REQUESTED,      // 인출 요청됨
    PROCESSING,     // 지급 처리 중
    COMPLETED,      // 지급 완료
    FAILED,         // 지급 실패
    CANCELED        // 요청 취소
}
