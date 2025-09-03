package com.forwork.backend.api.pay.service;

import com.forwork.backend.api.pay.entity.Payment;
import com.forwork.backend.api.pay.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentReader {
    private final PaymentRepository paymentRepository;


    /**
     * member 의 판매 내역을 조회한다
     */
    public Page<Payment> getSoldPayments(Long memberId, Pageable pageable) {
        Page<Long> paymentIdsByWriter = paymentRepository.findPaymentIdsByWriter(memberId, pageable);
        List<Long> paymentIds = paymentIdsByWriter.getContent();

        List<Payment> allById = paymentRepository.findByPaymentIdsWithOrder(paymentIds);

        // key: paymentId value: Payment
        Map<Long, Payment> idToPaymentMap = new HashMap<>();
        allById.forEach(payment -> idToPaymentMap.put(payment.getId(), payment));

        Page<Payment> payments = paymentIdsByWriter.map(paymentId -> {
            Payment payment = idToPaymentMap.get(paymentId);
            return payment;
        });

        return payments;
    }

    /**
     * member 의 총 판매 수익을 조회
     */
    public BigDecimal getTotalSalesRevenue(Long memberId) {
        BigDecimal totalSalesRevenue = paymentRepository.findTotalSalesRevenue(memberId);

        return totalSalesRevenue;
    }
}
