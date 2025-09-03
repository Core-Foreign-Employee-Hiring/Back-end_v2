package com.forwork.backend.api.pass_archive.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.order.repository.OrderRepository;
import com.forwork.backend.api.pass_archive.dto.ArchiveReviewDetailResponseDTO;
import com.forwork.backend.api.pass_archive.dto.ArchiveReviewRequestDTO;
import com.forwork.backend.api.pass_archive.dto.ArchiveReviewResponseDTO;
import com.forwork.backend.api.pass_archive.entity.ArchiveReview;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.ArchiveReviewRepository;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
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

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveReviewService {
    private final ArchiveReviewRepository archiveReviewRepository;
    private final PassArchiveRepository passArchiveRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;



    /*
    * c
    * */

    /**
     * 아카이브 리뷰 생성
     */
    @Transactional
    public void createArchiveReview(Long writerId,Long archiveId, ArchiveReviewRequestDTO archiveReviewRequestDTO) {

        // 구매했는지
        boolean purchased = orderRepository.existsPurchasedArchive(writerId, archiveId);
        if(!purchased){throw new BadRequestException(ARCHIVE_PURCHASE_FORBIDDEN_EXCEPTION.getMessage());}

        // 이전에 작성했는지 확인 리뷰 단위 구매? 주문? 아카이브?
        boolean written = archiveReviewRepository.existsByWriterIdAndArchiveId(writerId, archiveId);
        if(written){throw new BadRequestException(REVIEW_ALREADY_WRITTEN_EXCEPTION.getMessage());}

        Member writer = memberRepository.findById(writerId)
                .orElseThrow(() -> {
                    log.warn("[createArchiveReview][멤버 없음.][writerId={}]", writerId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        PassArchive passArchive = passArchiveRepository.findById(archiveId)
                .orElseThrow(() -> {
                    log.warn("[createArchiveReview][아카이브 없음.][archiveId={}]", archiveId);
                    return new NotFoundException(PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage());
                });

        // 리뷰 생성
        ArchiveReview archiveReview = ArchiveReview.builder()
                .star(archiveReviewRequestDTO.star())
                .content(archiveReviewRequestDTO.content())
                .writer(writer)
                .passArchive(passArchive)
                .build();

        archiveReviewRepository.save(archiveReview);

        // 아카이브 업데이트
        passArchiveRepository.updateStarCountByPassArchiveId(passArchive.getPassArchiveId());
        passArchiveRepository.updateStarByPassArchiveId(passArchive.getPassArchiveId(), passArchive.getStar());
    }


    /*
    * r
    * */

    /**
     * 아카이브 리뷰 페이징
     */

    public PageResponseDTO<ArchiveReviewResponseDTO> getArchiveReviews(Long archiveId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ArchiveReview> archiveReviews = archiveReviewRepository.findAllByArchiveId(archiveId, pageable);
        // responseDTO 로 변환
        Page<ArchiveReviewResponseDTO> dtos = archiveReviews.map(ArchiveReviewResponseDTO::of);
        PageResponseDTO<ArchiveReviewResponseDTO> response = PageResponseDTO.of(dtos);

        return response;
    }


    /**
     * 리뷰 상세 조회
     */
    public ArchiveReviewDetailResponseDTO getArchiveReview(Long reviewId){
        ArchiveReview archiveReview = archiveReviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.warn("[getArchiveReview][리뷰 없음.][reviewId={}]", reviewId);
                    return new NotFoundException(REVIEW_NOT_FOUND_EXCEPTION.getMessage());
                });

        ArchiveReviewDetailResponseDTO response = ArchiveReviewDetailResponseDTO.of(archiveReview);

        return response;
    }
}
