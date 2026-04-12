package com.forwork.backend.api.pay.service;

import com.forwork.backend.api.order.entity.Order;
import com.forwork.backend.api.order.entity.OrderPassArchive;
import com.forwork.backend.api.order.enums.ItemType;
import com.forwork.backend.api.order.repository.OrderPassArchiveRepository;
import com.forwork.backend.api.order.repository.OrderRepository;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.ArchiveDownloadHistoryRepository;
import com.forwork.backend.api.pay.dto.internal.PaymentDTO;
import com.forwork.backend.api.pay.dto.request.PaymentConfirmRequestDTO;
import com.forwork.backend.api.pay.dto.response.ArchivePaymentHistoryResponse;
import com.forwork.backend.api.pay.dto.response.PaymentHistoryResponse;
import com.forwork.backend.api.pay.entity.Payment;
import com.forwork.backend.api.pay.enums.PaymentStatus;
import com.forwork.backend.api.pay.event.PaymentCompletedEvent;
import com.forwork.backend.api.pay.exception.PaymentTimeoutException;
import com.forwork.backend.api.pay.exception.confirm.PaymentAbortedException;
import com.forwork.backend.api.pay.exception.confirm.PaymentAlreadyDoneException;
import com.forwork.backend.api.pay.exception.confirm.PaymentExpiredException;
import com.forwork.backend.api.pay.repository.PaymentRepository;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentCreator paymentCreator;
    private final TossPaymentClient tossPaymentClient;
    private final PaymentProcessor paymentProcessor;
    private final PaymentReader paymentReader;
    private final ArchiveDownloadHistoryRepository archiveDownloadHistoryRepository;
    private final OrderPassArchiveRepository orderPassArchiveRepository;
    private final List<PaymentHistoryStrategy> paymentHistoryStrategies;
    private final ApplicationEventPublisher eventPublisher;


    /**
     * 결제 상태는 반드시 IN_PROGRESS 로 시작하여 성공(DONE), 실패(ABORTED, EXPIRED), 타임아웃(TIMEOUT) 중 하나로 끝나야 한다.
     * <p>
     * 1) 구입할 수 있는지 각종 검증: amount, 최초 요청 등
     * 2) 결제 승인 api 호출
     * 3) db 반영
     * <p>
     * <p>
     * 1)
     * amount 검증.
     * 최초 요청만 통과
     * <p>
     * 2) 결제 승인 api 호출
     * 결제 승인 결과: 성공(ALREADY_DONE, DONE), 실패(ABORTED, EXPIRED), 타임아웃(TIMEOUT)
     * 실패: 결제 승인 x 확신 -> 상품 관련 만 rollback
     * 타임아웃: 결제 승인 유무 모름 -> 후보정
     * <p>
     * 3) db 반영
     * <p>
     * =============
     * <p>
     * 결제 승인 성공은 반드시 이 api 를 통해서만 할 것.
     */

    public void requestConfirm(Long memberId, PaymentConfirmRequestDTO paymentConfirmRequestDTO) {
        String paymentKey = paymentConfirmRequestDTO.paymentKey();
        String merchantOrderId = paymentConfirmRequestDTO.merchantOrderId();
        String amount = paymentConfirmRequestDTO.amount();

        // 1)

        amountValidate(amount, merchantOrderId);

        paymentRepository.findByPaymentKey(paymentKey)
                .ifPresentOrElse(
                        p -> {
                            log.warn("[requestConfirm][paymentKey 이미 존재][paymentKey={}]", paymentKey);
                            throw new BadRequestException(PAYMENT_ALREADY_EXISTS_EXCEPTION.getMessage());
                        },
                        () -> paymentCreator.create(paymentConfirmRequestDTO)
                );


        /*
         * 여기에  update 가 필요한 검증.
         */

        try {
            // 2
            PaymentDTO paymentDTO = tossPaymentClient.confirmPayment(paymentKey, merchantOrderId, Long.valueOf(amount));

            // 3
            paymentProcessor.completePayment(paymentDTO);

        } catch (PaymentAlreadyDoneException e) {
            PaymentDTO paymentDTO = tossPaymentClient.getPaymentByPaymentKey(paymentKey);
            paymentProcessor.completePayment(paymentDTO);

        } catch (PaymentAbortedException e) {
            paymentRepository.updatePaymentStatusByPaymentKey(paymentKey, PaymentStatus.ABORTED);
            throw e;

        } catch (PaymentExpiredException e) {
            paymentRepository.updatePaymentStatusByPaymentKey(paymentKey, PaymentStatus.EXPIRED);
            throw e;

        } catch (PaymentTimeoutException e) {
            paymentRepository.updatePaymentStatusByPaymentKey(paymentKey, PaymentStatus.TIMEOUT);
            throw e;
        } catch (Exception e) {
            log.error("[requestConfirm][알 수 없는 오류]", e);
            throw e;
        }

        eventPublisher.publishEvent(new PaymentCompletedEvent(memberId, paymentConfirmRequestDTO.merchantOrderId()));
    }

    private void amountValidate(String amount, String merchantOrderId) {
        String findAmount = orderRepository.findAmountByMerchantOrderId(merchantOrderId);

        if (findAmount == null || findAmount.isEmpty()) {
            log.warn("[requestConfirm][주문도 안 했는데 벌써 결제를 해?][merchantOrderId= {}]", merchantOrderId);
            throw new BadRequestException(ALREADY_DONE_PAYMENT_BEFORE_ORDER_EXCEPTION.getMessage());
        }

        if (!findAmount.equals(amount)) {
            log.warn("[requestConfirm][amount error][findAmount= {}, requestAmount= {}]", findAmount, amount);
            throw new BadRequestException("결제 처음부터 다시 시도.");
        }
    }


    /*
     * read
     * */


    /**
     * 아카이브 결제 내역 조회
     */
    public PageResponseDTO<ArchivePaymentHistoryResponse> getArchivePaymentHistory(Long memberId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        // 결제 내역 조회
        Page<Payment> paymentHistory = paymentReader.getPaymentHistory(memberId, pageable);


        // 아카이브 조회
        List<Long> orderIds = paymentHistory
                .map(Payment::getOrder)
                .map(Order::getId)
                .toList();

        List<OrderPassArchive> orderPassArchives = orderPassArchiveRepository.findAllByOrderIds(orderIds);

        // key: orderId, value: archive
        Map<Long, PassArchive> orderIdToPassArchiveMap = orderPassArchives.stream()
                .collect(Collectors.toMap(
                        opa -> opa.getOrder().getId(),
                        OrderPassArchive::getPassArchive,
                        (existing, replacement) -> replacement
                ));


        // 다운로드한 아카이브 id 조회
        List<Long> archiveIds = archiveDownloadHistoryRepository.findPassArchiveIdsByBuyerId(memberId);
        Set<Long> downloadedArchiveIdSet = new HashSet<>(archiveIds);

        // dto 변환
        Page<ArchivePaymentHistoryResponse> paymentHistoryResponses = paymentHistory.map((h) -> {
            Long orderId = h.getOrder().getId();
            PassArchive archive = orderIdToPassArchiveMap.get(orderId);

            boolean downloaded = downloadedArchiveIdSet.contains(
                    archive.getPassArchiveId()
            );


            return ArchivePaymentHistoryResponse.of(h, archive, downloaded);
        });

        PageResponseDTO<ArchivePaymentHistoryResponse> response = PageResponseDTO.of(paymentHistoryResponses);

        return response;
    }


    /**
     * 결제 내역 조회
     */
    public PageResponseDTO<PaymentHistoryResponse> getPaymentHistory(Long memberId, ItemType itemType, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Long> paymentIdPage = paymentRepository.findPaymentIdsByBuyerIdAndItemType(memberId, itemType.getValue(), pageable);
        List<Long> paymentIds = paymentIdPage.getContent();

        List<Payment> payments = paymentRepository.findWithOrderByIds(paymentIds);


        PaymentHistoryStrategy paymentHistoryStrategy = paymentHistoryStrategies.stream()
                .filter(s -> s.supports(itemType))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("[getPaymentHistory][전략 없음][itemType= {}]", itemType);
                    return new InternalServerException(INTERNAL_SERVER_EXCEPTION.getMessage());
                });


        List<PaymentHistoryResponse> map = paymentHistoryStrategy.map(payments);


        PageResponseDTO<PaymentHistoryResponse> response = PageResponseDTO.of(paymentIdPage, map);

        return response;
    }
}
