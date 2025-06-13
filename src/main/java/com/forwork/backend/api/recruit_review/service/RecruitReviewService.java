package com.forwork.backend.api.recruit_review.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.recruit_review.dto.internal.RecruitReviewPreviewInternalDTO;
import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewCreateDTO;
import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewUpdateDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewDetailResponseDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewPreviewResponseDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewTotalCountResponseDTO;
import com.forwork.backend.api.recruit_review.entity.RecruitReview;
import com.forwork.backend.api.recruit_review.enums.RecruitReviewSortType;
import com.forwork.backend.api.recruit_review.repository.RecruitReviewCommentRepository;
import com.forwork.backend.api.recruit_review.repository.RecruitReviewRepository;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitReviewService {
    private final RecruitReviewRepository recruitReviewRepository;
    private final MemberRepository memberRepository;
    private final RecruitReviewCommentRepository recruitReviewCommentRepository;

    /*
    * c
    * */

    /**
     * @apiNote
     * 채용 후기 생성
     *
     */
    public Long createRecruitReview(Long memberId, RecruitReviewCreateDTO dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[createRecruitReview][member is not found][memberId= {}]", memberId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        RecruitReview recruitReview = dto.toEntity(member);

        RecruitReview review = recruitReviewRepository.save(recruitReview);

        // readCountTracker 에 등록.
//        readCountTracker.init(review.getId(), review.getCreatedAt());

        return review.getId();
    }


    /*
    * r
    * */

    /**
     * @apiNote 채용 후기 단건 조회
     */
    public RecruitReviewDetailResponseDTO getRecruitReview(Long memberId, Long recruitReviewId) {
        // 조회수 증가 및 조회
        Integer readCount = incrementReadCount(recruitReviewId);

        RecruitReview recruitReview = recruitReviewRepository.findByIdWithWriter(recruitReviewId)
                .orElseThrow(() -> {
                    log.warn("[getRecruitReview][recruitReview is not found][recruitReviewId= {}]", recruitReviewId);
                    return new NotFoundException(RECRUIT_REVIEW_NOT_FOUND_EXCEPTION.getMessage());
                });

        Member writer = recruitReview.getWriter();

        // 댓글 수
        long commentCount=recruitReviewCommentRepository.findCommentCount(recruitReviewId);;

        // isMine
        boolean isMin= Objects.equals(memberId, writer.getId());

        RecruitReviewDetailResponseDTO response = RecruitReviewDetailResponseDTO.of(recruitReview, commentCount, isMin, writer, readCount);

        return response;
    }

    /**
     * @apiNote 채용 후기 페이징
     */
    public PageResponseDTO<RecruitReviewPreviewResponseDTO> getRecruitPreviews(String keyword, Integer page, Integer size, RecruitReviewSortType sortType) {

        Pageable pageable= PageRequest.of(page, size);

        Page<RecruitReviewPreviewInternalDTO> recruitPreviews = recruitReviewRepository.getRecruitPreviews(keyword, pageable, sortType);

        /*// tracker 에 존재하는 조회수 갖고 온다.
        List<Long> ids=recruitPreviews.stream()
                .map(RecruitReviewPreviewInternalDTO::recruitReviewId)
                .toList();

        // key: recruitReviewId, readCount
        Map<Long, Integer> readCounts = readCountTracker.getReadCounts(ids);*/

        Page<RecruitReviewPreviewResponseDTO> map = recruitPreviews
                                                    .map(RecruitReviewPreviewResponseDTO::of);

        PageResponseDTO<RecruitReviewPreviewResponseDTO> response = PageResponseDTO.of(map);

        return response;
    }

    /**
     * @apiNote  후기 totalCount 조회
     */
    public RecruitReviewTotalCountResponseDTO getRecruitReviewTotalCount(){
        Long l = recruitReviewRepository.fineToTalCount();

        return new RecruitReviewTotalCountResponseDTO(l);
    }

    /*
    * c
    * */

    /**
     * @apiNote 후기 수정
     */
    @Transactional
    public void updateRecruitReview(Long memberId, Long recruitReviewId, RecruitReviewUpdateDTO dto) {
        RecruitReview recruitReview = recruitReviewRepository.findByIdWithWriter(recruitReviewId)
                .orElseThrow(() -> {
                    log.warn("[updateRecruitReview][recruitReview is not found][recruitReviewId= {}]", recruitReviewId);
                    return new NotFoundException(RECRUIT_REVIEW_NOT_FOUND_EXCEPTION.getMessage());
                });

        validateRecruitReviewOwnership(memberId, recruitReview);

        recruitReview.update(dto);
    }

    /*
     * d
     * */

    /**
     * @apiNote 채용 후기 삭제
     */
    @Transactional
    public void deleteRecruitReview(Long memberId, Long recruitReviewId) {
        RecruitReview recruitReview = recruitReviewRepository.findByIdWithWriter(recruitReviewId)
                .orElseThrow(() -> {
                    log.warn("[deleteRecruitReview][recruitReview is not found][recruitReviewId= {}]", recruitReviewId);
                    return new NotFoundException(RECRUIT_REVIEW_NOT_FOUND_EXCEPTION.getMessage());
                });

        validateRecruitReviewOwnership(memberId, recruitReview);

        recruitReview.delete();

    }



    private void validateRecruitReviewOwnership(Long employerId, RecruitReview recruitReview) {
        if (!recruitReview.getWriter().getId().equals(employerId)) {
            log.warn("[validateRecruitReviewOwnership][소유자가 아님.][소유자= {}, 지금 사용자= {}]", recruitReview.getWriter().getId(), employerId);
            throw new BadRequestException(RECRUIT_REVIEW_OWNER_FORBIDDEN_EXCEPTION.getMessage());
        }
    }

    private Integer incrementReadCount(Long recruitReviewId) {
        int readCount = -1;

//        readCount = readCountTracker.increment(recruitReviewId);

        if (readCount == -1) {
            recruitReviewRepository.incrementReadCount(recruitReviewId);
        }

        return readCount;
    }


}
