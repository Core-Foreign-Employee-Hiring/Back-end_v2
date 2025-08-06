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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeMyPageService {
    private final OrderRepository orderRepository;
    private final PassArchiveRepository passArchiveRepository;



    public PageResponseDTO<PurchasedArchivesPreviewResponseDTO> getPurchasedArchives(Long memberId, Integer page, Integer size){
        Pageable pageable= PageRequest.of(page, size);

        Page<PassArchivePreviewIdAndPaymentApprovedAtQueryDTO> dtos = orderRepository.findMyArchiveByMemberId(memberId, pageable);

        List<Long> ids = dtos.getContent().stream()
                .map(PassArchivePreviewIdAndPaymentApprovedAtQueryDTO::passArchiveId)
                .toList();

        List<PassArchive> passArchives = passArchiveRepository.findAllByIds(ids);


        List<PurchasedArchivesPreviewResponseDTO> content = new ArrayList<>();

        dtos.getContent().forEach((PassArchivePreviewIdAndPaymentApprovedAtQueryDTO e) ->{
            Long passArchiveId = e.passArchiveId();
            LocalDate approvedAt = e.approvedAt().toLocalDate();

            PassArchive matchedPassArchive = passArchives.stream()
                    .filter(pa -> pa.getPassArchiveId().equals(passArchiveId))
                    .findFirst()
                    .orElse(null);


            PurchasedArchivesPreviewResponseDTO purchasedArchivesPreviewResponseDTO = PurchasedArchivesPreviewResponseDTO.of(matchedPassArchive, approvedAt);

            content.add(purchasedArchivesPreviewResponseDTO);
        });


        PageResponseDTO<PurchasedArchivesPreviewResponseDTO> response = PageResponseDTO.of(content, dtos.getNumber(), dtos.getSize(), dtos.getTotalElements(), dtos.getTotalPages());


        return response;

    }

}
