package com.forwork.backend.api.pay.service;

import com.forwork.backend.api.item.enums.ItemType;
import com.forwork.backend.api.pay.dto.response.PaymentHistoryResponse;
import com.forwork.backend.api.pay.entity.Payment;

import java.util.List;

public interface PaymentHistoryStrategy {
    boolean supports(ItemType type);

    List<PaymentHistoryResponse> map(List<Payment> payments);
}
