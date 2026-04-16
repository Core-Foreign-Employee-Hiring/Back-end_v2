package com.forwork.backend.api.pay.service;

import com.forwork.backend.api.item.enums.ItemType;
import com.forwork.backend.api.pay.dto.response.PaymentHistoryResponse;
import com.forwork.backend.api.pay.dto.response.PlanPaymentHistoryResponse;
import com.forwork.backend.api.pay.entity.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlanPaymentHistoryStrategy implements PaymentHistoryStrategy {


    @Override
    public boolean supports(ItemType type) {
        return ItemType.PLAN.equals(type);
    }

    @Override
    public List<PaymentHistoryResponse> map(List<Payment> payments) {

        List<PaymentHistoryResponse> responses =
                payments.stream()
                        .<PaymentHistoryResponse>map(PlanPaymentHistoryResponse::of)
                        .toList();

        return responses;
    }
}
