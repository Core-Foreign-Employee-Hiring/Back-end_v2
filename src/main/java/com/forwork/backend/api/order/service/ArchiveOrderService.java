package com.forwork.backend.api.order.service;


import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.order.dto.request.CashReceiptIssueRequest;
import com.forwork.backend.api.order.dto.request.OrderRequestDTO;
import com.forwork.backend.api.order.dto.response.CashReceiptResponse;
import com.forwork.backend.api.order.dto.response.OrderPreviewResponse;
import com.forwork.backend.api.order.dto.response.OrderResponseDTO;
import com.forwork.backend.api.order.entity.Order;
import com.forwork.backend.api.order.entity.OrderPassArchive;
import com.forwork.backend.api.order.repository.OrderPassArchiveRepository;
import com.forwork.backend.api.order.repository.OrderRepository;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
import com.forwork.backend.api.pay.dto.external.request.CashReceiptRequest;
import com.forwork.backend.api.pay.dto.external.response.TossCashReceiptResponse;
import com.forwork.backend.api.pay.entity.CashReceipt;
import com.forwork.backend.api.pay.repository.CashReceiptRepository;
import com.forwork.backend.api.pay.service.TossCashReceiptClient;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.ForbiddenException;
import com.forwork.backend.common.exception.InternalServerException;
import com.forwork.backend.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveOrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final PassArchiveRepository passArchiveRepository;
    private final OrderPassArchiveRepository orderPassArchiveRepository;
    private static final int MERCHANT_ORDER_THRESHOLD = 10;
    private final TossCashReceiptClient tossCashReceiptClient;
    private final CashReceiptRepository cashReceiptRepository;


    /*
     * c
     * */

    @Transactional
    public OrderResponseDTO createOrder(Long buyerId, OrderRequestDTO orderRequestDTO) {
        Member buyer = memberRepository.findById(buyerId)
                .orElseThrow(() -> {
                    log.warn("[createOrder][멤버 없음.][buyerId={}]", buyerId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        List<Long> requestedIds = orderRequestDTO.passArchiveIds();
        List<PassArchive> passArchives = passArchiveRepository.findAllById(requestedIds);

        // 실제 DB에 있는 ID
        List<Long> foundIds = passArchives.stream()
                .map(PassArchive::getPassArchiveId)
                .toList();

        // 없는 ID만 추출
        List<Long> missingIds = requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            log.warn("[createOrder][이상한 passArchiveIds 넘겼음.][passArchiveIds= {}]", missingIds);
            throw new NotFoundException(PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage());
        }

        /*
         * 필요하면 여기서 order 관련 각종 권한 처리. update 필요한 작업은 x
         * */

        // merchantOrderId 생성
        String merchantOrderId = MerchantOrderIdGenerator.generate();

        for (int i = 0; i < MERCHANT_ORDER_THRESHOLD; i++) {
            if (orderRepository.existsByMerchantOrderId(merchantOrderId)) {
                merchantOrderId = MerchantOrderIdGenerator.generate();
            } else {
                break;
            }
        }

        // orderName 생성
        String orderName = createOrderName(passArchives);

        // 총 금액
        long sum = passArchives.stream()
                .mapToLong(PassArchive::getPrice)
                .sum();
        String amount = String.valueOf(sum);

        try {
            // order 생성.
            Order order = Order.builder()
                    .merchantOrderId(merchantOrderId)
                    .orderName(orderName)
                    .amount(amount)
                    .buyer(buyer)
                    .build();

            orderRepository.save(order).getId();

        } catch (DataIntegrityViolationException e) {
            // 프론트에게 재시도 유도
            log.warn("[createOrder][중복 merchantOrderId][merchantOrderId={}]", merchantOrderId, e);
            throw new InternalServerException(INTERNAL_SERVER_EXCEPTION.getMessage());
        }

        Order order = orderRepository.findByMerchantOrderId(merchantOrderId).get();

        passArchives.forEach(
                (passArchive) -> {
                    OrderPassArchive orderPassArchive = OrderPassArchive.builder()
                            .amount(String.valueOf(passArchive.getPrice()))
                            .order(order)
                            .passArchive(passArchive)
                            .build();

                    orderPassArchiveRepository.save(orderPassArchive);
                });


        OrderResponseDTO response = getOrder(buyerId, merchantOrderId);

        return response;
    }

    private String createOrderName(List<PassArchive> passArchives) {


        String firstProductName = passArchives.get(0).getTitle();

        if (passArchives.size() == 1) {
            return truncate(firstProductName, 100);
        }

        String orderName = firstProductName + " 외 " + (passArchives.size() - 1) + "건";
        return truncate(orderName, 100);
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength);
    }

    /**
     * 현금영수증 발급
     */

    public void issueCashReceipt(Long memberId, CashReceiptIssueRequest request, String orderId) {

        // 소유자 검증
        Order order = orderRepository.findByMerchantOrderIdWithBuyer(orderId)
                .orElseThrow(() -> {
                    log.warn("[issueCashReceipt][주문 없음.][merchantOrderId={}]", orderId);
                    return new NotFoundException(ORDER_NOT_FOUND_EXCEPTION.getMessage());
                });

        Member buyer = order.getBuyer();

        if (buyer == null || !Objects.equals(buyer.getId(), memberId)) {
            Long buyerId = Optional.ofNullable(buyer).map(Member::getId).orElse(null);

            log.warn("[issueCashReceipt][소유자 검증 실패][buyerId= {}]", buyerId);
            throw new ForbiddenException(ORDER_ACCESS_DENIED_EXCEPTION.getMessage());
        }

        // 현금영수증 발급
        CashReceiptRequest cashReceiptRequest = CashReceiptRequest.of(order, request.type(), request.customerIdentityNumber(), null);

        TossCashReceiptResponse tossCashReceiptResponse = tossCashReceiptClient.issueCashReceipt(cashReceiptRequest);


        // db 저장
        CashReceipt entity = tossCashReceiptResponse.toEntity();


        cashReceiptRepository.save(entity);

    }

    /*
     * r
     * */

    /**
     * 주문 정보 조회
     */
    public OrderResponseDTO getOrder(Long memberId, String merchantOrderId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[getOrder][멤버 없음.][memberId={}]", memberId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        // 주문 조회
        Order order = orderRepository.findByMerchantOrderIdWithBuyer(merchantOrderId)
                .orElseThrow(() -> {
                    log.warn("[getOrder][주문 없음.][merchantOrderId={}]", merchantOrderId);
                    return new NotFoundException(ORDER_NOT_FOUND_EXCEPTION.getMessage());
                });

        // 소유자 검증
        if (!order.getBuyer().getId().equals(memberId)) {
            log.warn("[getOrder][소유자 이상해][memberId={}, buyerId= {}]", memberId, order.getBuyer().getId());
            throw new BadRequestException(ORDER_ACCESS_DENIED_EXCEPTION.getMessage());
        }

        // 아카이브 갖고 온다.
        List<OrderPassArchive> orderPassArchives = orderPassArchiveRepository.findAllByOrderIdWithPassArchive(order.getId());

        // 일단, 요구사항이 아카이브 한 개만 구매 가능이라 ㄱㅊ
        Long passArchiveId = orderPassArchives.get(0).getPassArchive().getPassArchiveId();

        // 썸네일 갖고 와야 하는데
        PassArchive passArchive = passArchiveRepository.findArchiveByPassArchiveIdWithThumbnail(passArchiveId)
                .orElseThrow(() -> {
                    log.warn("[getOrder][아카이브 없음][passArchiveId={}]", passArchiveId);
                    return new NotFoundException(PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage());
                });

        OrderResponseDTO response = OrderResponseDTO.of(member, order, passArchive);

        return response;
    }

    /**
     * 현금영수증 조회
     */

    public CashReceiptResponse getCashReceipt(Long memberId, String merchantOrderId) {
        // 소유자 검증
        Order order = orderRepository.findByMerchantOrderIdWithBuyer(merchantOrderId)
                .orElseThrow(() -> {
                    log.warn("[getCashReceipt][주문 없음.][merchantOrderId={}]", merchantOrderId);
                    return new NotFoundException(ORDER_NOT_FOUND_EXCEPTION.getMessage());
                });

        Member buyer = order.getBuyer();

        if (buyer == null || !Objects.equals(buyer.getId(), memberId)) {
            Long buyerId = Optional.ofNullable(buyer).map(Member::getId).orElse(null);

            log.warn("[getCashReceipt][소유자 검증 실패][buyerId= {}]", buyerId);
            throw new ForbiddenException(ORDER_ACCESS_DENIED_EXCEPTION.getMessage());
        }


        CashReceipt cashReceipt = cashReceiptRepository.findByMerchantOrderId(merchantOrderId)
                .orElseThrow(() -> {
                    log.warn("[getCashReceipt][현금영수증 우리 DB에 없음.][merchantOrderId={}]", merchantOrderId);
                    return new NotFoundException(CASH_RECEIPT_NOT_FOUND_EXCEPTION.getMessage());
                });


        CashReceiptResponse response = new CashReceiptResponse(cashReceipt.getReceiptUrl());

        return response;
    }

    /**
     * 주문 미리보기 조회
     */

    public OrderPreviewResponse getOrderPreview(Long memberId, Long passArchiveId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[getOrderPreview][멤버 없음.][memberId={}]", memberId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });


        PassArchive passArchive = passArchiveRepository.findArchiveByPassArchiveIdWithThumbnail(passArchiveId)
                .orElseThrow(() -> {
                    log.warn("[getOrderPreview][아카이브 없음][passArchiveId={}]", passArchiveId);
                    return new NotFoundException(PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage());
                });


        OrderPreviewResponse response = OrderPreviewResponse.of(passArchive, member);

        return response;
    }
}
