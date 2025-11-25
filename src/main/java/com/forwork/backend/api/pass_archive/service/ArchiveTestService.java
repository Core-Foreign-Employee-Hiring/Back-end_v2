package com.forwork.backend.api.pass_archive.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.pass_archive.dto.ArchiveReviewRequestDTO;
import com.forwork.backend.api.pass_archive.entity.ArchiveReview;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.ArchiveReviewRepository;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.forwork.backend.common.response.ErrorStatus.PASS_ARCHIVE_NOT_FOUND_EXCEPTION;
import static com.forwork.backend.common.response.ErrorStatus.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveTestService {
    private final MemberRepository memberRepository;
    private final PassArchiveRepository passArchiveRepository;
    private final ArchiveReviewRepository archiveReviewRepository;


    /**
     * 결제 없이 리뷰 달기
     */
    @Transactional
    public void createArchiveReview(Long writerId,Long archiveId, ArchiveReviewRequestDTO archiveReviewRequestDTO) {
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

    /**
     * 아카이브 삭제
     */
    @Transactional
    public void deleteArchive(Long archiveId){
        Optional<PassArchive> byId = passArchiveRepository.findById(archiveId);

        if(byId.isPresent()){
            PassArchive passArchive = byId.get();
            passArchiveRepository.delete(passArchive);
        }

    }


    /**
     * 현재 아카이브 전체 삭제
     */
    @Transactional
    public void deleteAllArchives(){
        List<PassArchive> all = passArchiveRepository.findAll();

        passArchiveRepository.deleteAll(all);

    }
}