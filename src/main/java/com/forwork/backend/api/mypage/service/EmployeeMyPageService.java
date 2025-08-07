package com.forwork.backend.api.mypage.service;

import com.forwork.backend.api.mypage.dto.response.PurchasedArchivesPreviewResponseDTO;
import com.forwork.backend.api.order.dto.query.PassArchivePreviewIdAndPaymentApprovedAtQueryDTO;
import com.forwork.backend.api.order.repository.OrderRepository;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
import com.forwork.backend.common.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

        List<PurchasedArchivesPreviewResponseDTO> content = new ArrayList<>();

        /*
        * 정렬된 순서대로 유지하기 위해 dtos 를 loop 돌아야 함.
        * */
        dtos.getContent().forEach((PassArchivePreviewIdAndPaymentApprovedAtQueryDTO e) ->{
            Long passArchiveId = e.passArchiveId();
            LocalDate approvedAt = e.approvedAt().toLocalDate();

            // id에 맞는 PassArchive 찾아옴.
            PassArchive matchedPassArchive = passArchiveMap.get(passArchiveId);

            PurchasedArchivesPreviewResponseDTO purchasedArchivesPreviewResponseDTO = PurchasedArchivesPreviewResponseDTO.of(matchedPassArchive, approvedAt);

            content.add(purchasedArchivesPreviewResponseDTO);
        });

        PageResponseDTO<PurchasedArchivesPreviewResponseDTO> response = PageResponseDTO.of(content, dtos.getNumber(), dtos.getSize(), dtos.getTotalElements(), dtos.getTotalPages());
        return response;
    }
}
