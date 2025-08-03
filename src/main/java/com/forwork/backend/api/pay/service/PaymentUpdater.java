package com.forwork.backend.api.pay.service;

import com.forwork.backend.api.pay.dto.internal.PaymentDTO;
import com.forwork.backend.api.pay.entity.Payment;
import com.forwork.backend.api.pay.repository.PaymentRepository;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentUpdater {
    private final PaymentRepository paymentRepository;


    @Transactional
    public void updatePaymentAsDone(PaymentDTO paymentDTO){
        String paymentKey = paymentDTO.paymentKey();

        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> {
                    log.warn("[updatePaymentAsDone][payment 없음.][paymentKey={}]", paymentKey);
                    return new NotFoundException("payment 없음.");
                });

        payment.updatePayment(paymentDTO);
    }
}
