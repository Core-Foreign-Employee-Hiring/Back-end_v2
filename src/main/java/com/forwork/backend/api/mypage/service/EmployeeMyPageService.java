package com.forwork.backend.api.mypage.service;

import com.forwork.backend.api.mypage.dto.query.ArchiveSalesCountQueryDTO;
import com.forwork.backend.api.mypage.dto.response.ArchiveInquiryResponseDTO;
import com.forwork.backend.api.mypage.dto.response.PurchasedArchivesPreviewResponseDTO;
import com.forwork.backend.api.mypage.dto.response.SoldArchiveResponseDTO;
import com.forwork.backend.api.mypage.dto.response.WrittenArchiveResponseDTO;
import com.forwork.backend.api.order.dto.query.PassArchivePreviewIdAndPaymentApprovedAtQueryDTO;
import com.forwork.backend.api.order.entity.OrderPassArchive;
import com.forwork.backend.api.order.repository.OrderPassArchiveRepository;
import com.forwork.backend.api.order.repository.OrderRepository;
import com.forwork.backend.api.pass_archive.entity.ArchiveInquiry;
import com.forwork.backend.api.pass_archive.entity.ArchiveReview;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.ArchiveInquiryRepository;
import com.forwork.backend.api.pass_archive.repository.ArchiveReviewRepository;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
import com.forwork.backend.api.pass_archive.service.ArchiveInquiryReader;
import com.forwork.backend.api.pay.entity.Payment;
import com.forwork.backend.api.pay.repository.PaymentRepository;
import com.forwork.backend.api.pay.service.PaymentReader;
import com.forwork.backend.common.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeMyPageService {
    private final OrderRepository orderRepository;
    private final PassArchiveRepository passArchiveRepository;
    private final ArchiveReviewRepository archiveReviewRepository;
    private final ArchiveInquiryReader archiveInquiryReader;
    private final OrderPassArchiveRepository orderPassArchiveRepository;
    private final PaymentReader paymentReader;

    /*
    * r
    * */

    /**
     * 구매한 아카이브 목록 조회
     */
    public PageResponseDTO<PurchasedArchivesPreviewResponseDTO> getPurchasedArchives(Long memberId, Integer page, Integer size){
        Pageable pageable= PageRequest.of(page, size);

        /*
         * 아카이브 id, 결제 승인 날짜만 조회
         * 이유 1) 조인 많아짐. 2) 아카이브 1:N 상품 이라 데이터 뻥튀기됨. -> 제대로 페이징 안 될 수도.
         *
         * opa.id 가 auto_increment -> 대강 날짜 순 정렬임
         *
         */
        Page<PassArchivePreviewIdAndPaymentApprovedAtQueryDTO> dtos = orderRepository.findMyArchiveByMemberId(memberId, pageable);

        // 아카이브 id만 뽑아옴.
        List<Long> ids = dtos.getContent().stream()
                .map(PassArchivePreviewIdAndPaymentApprovedAtQueryDTO::passArchiveId)
                .toList();

        // 아카이브 조회
        List<PassArchive> passArchives = passArchiveRepository.findAllByIds(ids);

        // key:  아카이브 id, value: PassArchive
        Map<Long, PassArchive> passArchiveMap = new HashMap<>();
        for (PassArchive passArchive : passArchives) {
            passArchiveMap.put(passArchive.getPassArchiveId(), passArchive);
        }

        // 리뷰 조회
        List<ArchiveReview> archiveReviews = archiveReviewRepository.findAllByArchiveIds(ids);

        // key:  아카이브 id, value: ArchiveReview
        Map<Long, ArchiveReview> archiveReviewMap = new HashMap<>();
        for (ArchiveReview archiveReview : archiveReviews) {
            Long passArchiveId = archiveReview.getPassArchive().getPassArchiveId();

            archiveReviewMap.put(passArchiveId, archiveReview);
        }

        List<PurchasedArchivesPreviewResponseDTO> content = new ArrayList<>();

        /*
        * 정렬된 순서대로 유지하기 위해 dtos 를 loop 돌아야 함.
        * */
        dtos.getContent().forEach((PassArchivePreviewIdAndPaymentApprovedAtQueryDTO e) ->{
            Long passArchiveId = e.passArchiveId();
            LocalDate approvedAt = e.approvedAt().toLocalDate();

            // id에 맞는 PassArchive 찾아옴.
            PassArchive matchedPassArchive = passArchiveMap.get(passArchiveId);

            // id에 맞는 ArchiveReview 찾아옴.
            ArchiveReview archiveReview = archiveReviewMap.get(passArchiveId);

            PurchasedArchivesPreviewResponseDTO purchasedArchivesPreviewResponseDTO = PurchasedArchivesPreviewResponseDTO.of(matchedPassArchive, archiveReview, approvedAt);

            content.add(purchasedArchivesPreviewResponseDTO);
        });

        PageResponseDTO<PurchasedArchivesPreviewResponseDTO> response = PageResponseDTO.of(content, dtos.getNumber(), dtos.getSize(), dtos.getTotalElements(), dtos.getTotalPages());
        return response;
    }

    /**
     * 내가 보낸 문의 조희
     */
    public PageResponseDTO<ArchiveInquiryResponseDTO> getSentInquiries(Long inquirerId, Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page, size);
        Page<ArchiveInquiryResponseDTO> archiveInquiries = archiveInquiryReader.getSentInquiries(inquirerId, pageable)
                .map((ArchiveInquiry) -> ArchiveInquiryResponseDTO.of(inquirerId, ArchiveInquiry));
        PageResponseDTO<ArchiveInquiryResponseDTO> response = PageResponseDTO.of(archiveInquiries);
        return response;
    }

    /**
     * 내가 받은 문의 조희
     */
    public PageResponseDTO<ArchiveInquiryResponseDTO> getReceivedInquiries(Long receiverId, Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page, size);
        Page<ArchiveInquiryResponseDTO> archiveInquiries = archiveInquiryReader.getReceivedInquiries(receiverId, pageable)
                .map((ArchiveInquiry) -> ArchiveInquiryResponseDTO.of(receiverId, ArchiveInquiry));

        PageResponseDTO<ArchiveInquiryResponseDTO> response = PageResponseDTO.of(archiveInquiries);
        return response;
    }

    /**
     * 작성한 아키이브 조회
     */
    public PageResponseDTO<WrittenArchiveResponseDTO> getWrittenArchives(Long writerId, Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "passArchiveId"));

        // 작성한 아카이브 조회
        Page<PassArchive> allByWriterId = passArchiveRepository.findAllByWriterId(writerId, pageable);

        List<Long> ids = allByWriterId.getContent().stream()
                .map(PassArchive::getPassArchiveId)
                .toList();

        // 몇 개 판매되었는지.
        List<ArchiveSalesCountQueryDTO> salesCountsByArchiveIds = orderPassArchiveRepository.findSalesCountsByArchiveIds(ids);

        // key: archiveId value: salesCount
        Map<Long, Long> archiveIdToSalesCountMap = new HashMap<>();
        salesCountsByArchiveIds
                .forEach((archiveSalesCountQueryDTO -> archiveIdToSalesCountMap.put(archiveSalesCountQueryDTO.passArchiveId(), archiveSalesCountQueryDTO.salesCount())));

        // response 타입으로 변경
        Page<WrittenArchiveResponseDTO> dtos = allByWriterId
                .map((passArchive -> WrittenArchiveResponseDTO.of(passArchive, archiveIdToSalesCountMap.get(passArchive.getPassArchiveId()))));

        PageResponseDTO<WrittenArchiveResponseDTO> response = PageResponseDTO.of(dtos);

        return response;
    }

    /**
     * 판매한 아카이브 조회
     */
    public PageResponseDTO<SoldArchiveResponseDTO> getSoldArchives(Long memberId, Integer page, Integer size){
        Pageable pageable= PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        // 판매 내역(Payment) 조회
        Page<Payment> soldPayments = paymentReader.getSoldPayments(memberId, pageable);

        List<Long> orderIds = soldPayments.getContent().stream()
                .map(payment -> payment.getOrder().getId())
                .toList();

        // 주문_아카이브 조회
        List<OrderPassArchive> orderPassArchives = orderPassArchiveRepository.findAllByOrderIds(orderIds);

        // key: orderId value: archive
        Map<Long, PassArchive> passArchiveMap = new HashMap<>();
        orderPassArchives
                .forEach((orderPassArchive) -> passArchiveMap.put(orderPassArchive.getOrder().getId(), orderPassArchive.getPassArchive()));

        // 타입 변경
        Page<SoldArchiveResponseDTO> dtos = soldPayments
                .map(payment -> {
                    Long orderId = payment.getOrder().getId();
                    PassArchive passArchive = passArchiveMap.get(orderId);

                    return SoldArchiveResponseDTO.of(payment, passArchive);
                });

        PageResponseDTO<SoldArchiveResponseDTO> response = PageResponseDTO.of(dtos);

        return response;
    }

    /**
     * 판매한 총 수익 조회
     */
    public String getTotalSalesRevenue(Long memberId) {
        BigDecimal totalSalesRevenue = paymentReader.getTotalSalesRevenue(memberId);
        String response = totalSalesRevenue.setScale(2, RoundingMode.HALF_UP).toString();
        return response;
    }
}
