package com.forwork.backend.api.pass_archive.service;

import com.forwork.backend.api.pass_archive.entity.ArchiveInquiry;
import com.forwork.backend.api.pass_archive.repository.ArchiveInquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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
}
