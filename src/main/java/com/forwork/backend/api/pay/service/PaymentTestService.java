package com.forwork.backend.api.pay.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.order.dto.request.OrderRequestDTO;
import com.forwork.backend.api.order.dto.response.OrderResponseDTO;
import com.forwork.backend.api.order.repository.OrderPassArchiveRepository;
import com.forwork.backend.api.order.repository.OrderRepository;
import com.forwork.backend.api.order.service.OrderService;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
import com.forwork.backend.api.pay.dto.internal.PaymentDTO;
import com.forwork.backend.api.pay.dto.request.PaymentConfirmRequestDTO;
import com.forwork.backend.api.pay.enums.TossPaymentStatus;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.forwork.backend.common.response.ErrorStatus.PASS_ARCHIVE_NOT_FOUND_EXCEPTION;
import static com.forwork.backend.common.response.ErrorStatus.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentTestService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final PassArchiveRepository passArchiveRepository;
    private final OrderPassArchiveRepository orderPassArchiveRepository;
    private final PaymentProcessor paymentProcessor;
    private final PaymentCreator paymentCreator;
    private final OrderService orderService;


    /**
     * 결제 없이 아카이브 구매
     */

    @Transactional
    public OrderResponseDTO requestConfirm(Long buyerId, Long archiveId) {
        // 멤버 조회
        Member buyer = memberRepository.findById(buyerId)
                .orElseThrow(() -> {
                    log.warn("[requestConfirm][멤버 없음.][buyerId={}]", buyerId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        // 아카이브 조회
        PassArchive passArchive = passArchiveRepository.findById(archiveId)
                .orElseThrow(() -> {
                    log.warn("[requestConfirm][아카이브 없음.][archiveId={}]", buyerId);
                    return new NotFoundException(PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage());
                });

        /*
         * 주문
         * */

        // 주문 생성
        OrderResponseDTO order = orderService.createOrder(buyerId, new OrderRequestDTO(List.of(archiveId)));
        String merchantOrderId = order.merchantOrderId();


        /*
         * 결제
         * */


        // 초기 결제 생성
        String paymentKey = UUID.randomUUID().toString();

        PaymentConfirmRequestDTO paymentConfirmRequestDTO = new PaymentConfirmRequestDTO(
                paymentKey,
                merchantOrderId,
                String.valueOf(passArchive.getPrice()),
                true,
                true,
                true

        );

        paymentCreator.create(paymentConfirmRequestDTO);


        // 결제 완료
        PaymentDTO paymentDTO = new PaymentDTO(TossPaymentStatus.DONE,
                paymentKey,
                String.valueOf(passArchive.getPrice()),
                "method",
                OffsetDateTime.now(),
                "orderName"
        );

        paymentProcessor.completePayment(paymentDTO);

        return order;
    }
}