package com.forwork.backend.api.pay.service;

import com.forwork.backend.api.order.entity.Order;
import com.forwork.backend.api.order.repository.OrderRepository;
import com.forwork.backend.api.pay.entity.Payment;
import com.forwork.backend.api.pay.enums.PaymentStatus;
import com.forwork.backend.api.pay.repository.PaymentRepository;
import com.forwork.backend.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.forwork.backend.common.response.ErrorStatus.ALREADY_DONE_PAYMENT_BEFORE_ORDER_EXCEPTION;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCreator {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Payment create(String paymentKey, String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> {
                    log.warn("[requestConfirm][주문도 안 했는데 벌써 결제를 해?][merchantOrderId= {}]", orderId);
                    return new BadRequestException(ALREADY_DONE_PAYMENT_BEFORE_ORDER_EXCEPTION.getMessage());
                });

        Payment payment = Payment.builder()
                .paymentKey(paymentKey)
                .paymentStatus(PaymentStatus.IN_PROGRESS)
                .order(order)
                .build();

        Payment save = paymentRepository.save(payment);

        return save;
    }
}
