package com.forwork.backend.api.pass_archive.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.pass_archive.entity.ArchiveInquiry;
import com.forwork.backend.api.pass_archive.repository.ArchiveInquiryRepository;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.forwork.backend.common.response.ErrorStatus.INQUIRY_NOT_FOUND_EXCEPTION;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArchiveInquiryReader {
    private final ArchiveInquiryRepository archiveInquiryRepository;


    /**
     * 내가 보낸 문의 조희
     */
    public Page<ArchiveInquiry> getSentInquiries(Long inquirerId, Pageable pageable) {
        Page<ArchiveInquiry> archiveInquiries = archiveInquiryRepository.findSentInquiriesByInquirerId(inquirerId, pageable);
        return archiveInquiries;
    }

    /**
     * 내가 받은 문의 조희
     */
    public Page<ArchiveInquiry> getReceivedInquiries(Long receiverId, Pageable pageable) {
        Page<ArchiveInquiry> archiveInquiries = archiveInquiryRepository.findReceivedInquiriesByReceiverId(receiverId, pageable);
        return archiveInquiries;
    }

    /**
     * 문의 답변 유무 조회
     */
    public boolean isAnswered(Long archiveInquiryId){
        ArchiveInquiry archiveInquiry = archiveInquiryRepository.findById(archiveInquiryId)
                .orElseThrow(() -> {
                    log.warn("[isAnswered][문의 없음.][archiveInquiryId={}]", archiveInquiryId);
                    return new NotFoundException(INQUIRY_NOT_FOUND_EXCEPTION.getMessage());
                });

        return archiveInquiry.isAnswered();
    }

    /**
     * 내가 보낸 문의 중 가장 최근 거 조회
     */
    public ArchiveInquiry getLatestInquiry(Long inquirerId) {
        ArchiveInquiry archiveInquiry = archiveInquiryRepository.findLatestInquiryByArchiveInquiryIdAndInquirerId(inquirerId)
                .orElseThrow(() -> {
                    log.warn("[getLatestInquiry][문의 없음.][inquirerId={}]", inquirerId);
                    return new NotFoundException(INQUIRY_NOT_FOUND_EXCEPTION.getMessage());
                });

        return archiveInquiry;
    }

    /**
     * 특정 아카이브에 대해 읽지 않은 문의가 있어?
     */
    public boolean hasUnreadInquiryForArchive(Long archiveId) {
        return archiveInquiryRepository.existsUnreadInquiryForArchive(archiveId);
    }

    /**
     * 문의하기 조회
     */
    @Transactional
    public ArchiveInquiry getInquiry(Long memberId, Long inquiryId) {
        ArchiveInquiry archiveInquiry = archiveInquiryRepository.findByIdWithArchiveAndArchiveWriterAndInquirer(inquiryId)
                .orElseThrow(() -> {
                    log.warn("[getAnswer][문의 없음.][inquiryId={}]", inquiryId);
                    return new NotFoundException(INQUIRY_NOT_FOUND_EXCEPTION.getMessage());
                });

        // 읽음 처리.
        Member inquirer = archiveInquiry.getInquirer();
        Member archiveWriter = archiveInquiry.getArchive().getMember();

        if(inquirer.getId().equals(memberId)){
            archiveInquiry.readByInquirer();
        }
        else if(archiveWriter.getId().equals(memberId)){
            archiveInquiry.readByArchiveWriter();
        }

        return archiveInquiry;
    }
}
