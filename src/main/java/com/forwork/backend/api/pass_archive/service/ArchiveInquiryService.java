package com.forwork.backend.api.pass_archive.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.notification.entity.ArchiveInquiryNotification;
import com.forwork.backend.api.notification.enums.ArchiveInquiryNotificationType;
import com.forwork.backend.api.notification.repository.ArchiveInquiryNotificationRepository;
import com.forwork.backend.api.pass_archive.dto.ArchiveInquiryAnswerResponseDTO;
import com.forwork.backend.api.pass_archive.entity.ArchiveInquiry;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.ArchiveInquiryRepository;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveInquiryService {
    private final ArchiveInquiryRepository archiveInquiryRepository;
    private final MemberRepository memberRepository;
    private final PassArchiveRepository passArchiveRepository;
    private final ArchiveInquiryNotificationRepository archiveInquiryNotificationRepository;



    /*
     * c
     * */

    /**
     * 문의하기
     */

    @Transactional
    public void inquiry(Long inquirerId, Long archiveId, String content) {
        Member inquirer = memberRepository.findById(inquirerId)
                .orElseThrow(() -> {
                    log.warn("[inquiry][멤버 없음.][inquirerId={}]", inquirerId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        PassArchive passArchive = passArchiveRepository.findArchiveByArchiveIdWithMember(archiveId)
                .orElseThrow(() -> {
                    log.warn("[inquiry][아카이브 없음.][archiveId={}]", archiveId);
                    return new NotFoundException(PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage());
                });

        // 문의
        ArchiveInquiry archiveInquiry = ArchiveInquiry.builder()
                .archive(passArchive)
                .inquiry(content)
                .inquirer(inquirer)
                .build();

        archiveInquiryRepository.save(archiveInquiry);

        // 알림
        Member archiveOwner = passArchive.getMember();
        ArchiveInquiryNotification archiveInquiryNotification = new ArchiveInquiryNotification(ArchiveInquiryNotificationType.INQUIRY, archiveInquiry, archiveOwner);
        archiveInquiryNotificationRepository.save(archiveInquiryNotification);

    }

    /**
     * 답변하기
     */

    @Transactional
    public void answer(Long answererId, Long archiveInquiryId, String answer) {

        ArchiveInquiry archiveInquiry = archiveInquiryRepository.findByIdWithInquirerAndArchiveAndArchiveOwner(archiveInquiryId)
                .orElseThrow(() -> {
                    log.warn("[answer][문의 없음.][archiveInquiryId={}]", archiveInquiryId);
                    return new NotFoundException(INQUIRY_NOT_FOUND_EXCEPTION.getMessage());
                });


        Member archiveOwner = archiveInquiry.getArchive().getMember();

        if (!Objects.equals(archiveOwner.getId(), answererId)) {
            log.warn("[answer][다른 사람 아카이브 문의에 답변 x][answererId= {}, archiveOwnerId= {}]", answererId, archiveOwner.getId());
            throw new BadRequestException(UNAUTHORIZED_INQUIRY_ANSWER_EXCEPTION.getMessage());
        }

        if(archiveInquiry.getAnswer()!= null){
            log.warn("[answer][이미 답변했음][archiveInquiryId= {}]", archiveInquiryId);
            throw new BadRequestException(ANSWER_ALREADY_EXISTS_EXCEPTION.getMessage());
        }

        // 답변
        archiveInquiry.answer(answer);

        // 알림
        Member inquirer = archiveInquiry.getInquirer();
        ArchiveInquiryNotification archiveInquiryNotification = new ArchiveInquiryNotification(ArchiveInquiryNotificationType.ANSWER, archiveInquiry, inquirer);
        archiveInquiryNotificationRepository.save(archiveInquiryNotification);
    }

    /*
    * r
    * */

    /**
     * 답변보기
     */
    public ArchiveInquiryAnswerResponseDTO getAnswer(Long inquiryId) {
        ArchiveInquiry archiveInquiry = archiveInquiryRepository.findByIdWithArchive(inquiryId)
                .orElseThrow(() -> {
                    log.warn("[getAnswer][문의 없음.][inquiryId={}]", inquiryId);
                    return new NotFoundException(INQUIRY_NOT_FOUND_EXCEPTION.getMessage());
                });

        return ArchiveInquiryAnswerResponseDTO.of(archiveInquiry);
    }

}