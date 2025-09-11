package com.forwork.backend.api.pay.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.order.repository.OrderPassArchiveRepository;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
import com.forwork.backend.api.pay.dto.response.TestPayoutResponseDTO;
import com.forwork.backend.api.pay.entity.Payment;
import com.forwork.backend.api.pay.entity.Payout;
import com.forwork.backend.api.pay.enums.PayoutStatus;
import com.forwork.backend.api.pay.repository.PaymentRepository;
import com.forwork.backend.api.pay.repository.PayoutRepository;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutService {
    private final PaymentRepository paymentRepository;
    private final OrderPassArchiveRepository orderPassArchiveRepository;
    private final PassArchiveRepository passArchiveRepository;
    private final MemberRepository memberRepository;
    private final PayoutRepository payoutRepository;

    /*
    * c
    * */

    /**
     * 인출 요청
     */
    @Transactional
    public void requestPayout(Long requesterId, List<Long> paymentIds){

        /*
        * 해당 payment 가 seller 소유가 맞는지 확인해야 함.
        * */

        // order 조회
        List<Payment> payments = paymentRepository.findByPaymentIdsWithOrder(paymentIds);
        List<Long> orderIds = payments.stream()
                .map(payment -> payment.getOrder().getId())
                .toList();


        // 아카이브 조회
        List<Long> archiveIds = orderPassArchiveRepository.findArchiveIdsByOrderIds(orderIds);
        List<PassArchive> archives = passArchiveRepository.findAllByArchiveIdsWithSeller(archiveIds);

        // 소유자인지 확인
        archives.forEach(passArchive -> {
            Member seller = passArchive.getMember();

            if(!seller.getId().equals(requesterId)){
                log.warn("[requestPayout][다른 사람이 인출 시도][requesterId= {}, sellerId= {}]", requesterId, seller.getId());
                throw new BadRequestException(UNAUTHORIZED_PAYOUT_REQUEST_EXCEPTION.getMessage());
            }
        });

        /*
        * 이미 인출한 payment 인지
        * */

        // 이미 인출 요청한 paymentId 조회
        List<Long> requestedPaymentIds = paymentRepository.findPaymentIdsBySellerId(requesterId);

        // 이번 요청과 겹치는지 체크
        List<Long> duplicatePayments = paymentIds.stream()
                .filter(requestedPaymentIds::contains)
                .toList();

        if (!duplicatePayments.isEmpty()) {
            log.warn("[requestPayout][이미 요청된 결제 포함][requesterId={}, duplicatePayments={}]", requesterId, duplicatePayments);
            throw new BadRequestException(PAYOUT_ALREADY_REQUESTED_EXCEPTION.getMessage());
        }

        /*
        * 인출 요청
        * */

        Member seller = memberRepository.findById(requesterId)
                .orElseThrow(() -> {
                    log.warn("[requestPayout][멤버 없음.][requesterId={}]", requesterId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        BigDecimal totalAmount = payments.stream()
                .map(payment -> new BigDecimal(payment.getTotalAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Payout payout = Payout.builder()
                .totalAmount(totalAmount.toString())
                .payoutStatus(PayoutStatus.REQUESTED)
                .seller(seller)
                .build();

        payoutRepository.save(payout);


        payments.forEach(payment -> payment.requestPayout(payout));
    }

    /*
     * 테스트
     * */

    /**
     * payout 조회
     */
    public PageResponseDTO<TestPayoutResponseDTO> getPayouts(Long sellerId, PayoutStatus status, Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page, size);

        Page<Payout> payouts = payoutRepository.findBySellerIdAndPayoutStatus(sellerId, status, pageable);
        Page<TestPayoutResponseDTO> dtos = payouts.map(TestPayoutResponseDTO::of);

        PageResponseDTO<TestPayoutResponseDTO> response = PageResponseDTO.of(dtos);

        return response;
    }

}
