package com.forwork.backend.api.pass_archive.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.notification.entity.ArchiveInquiryNotification;
import com.forwork.backend.api.notification.enums.ArchiveInquiryNotificationType;
import com.forwork.backend.api.notification.repository.ArchiveInquiryNotificationRepository;
import com.forwork.backend.api.pass_archive.dto.ArchiveInquiryResponseDTO;
import com.forwork.backend.api.pass_archive.dto.LatestInquiryResponseDTO;
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
    private final ArchiveInquiryReader archiveInquiryReader;



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
                .isAnswered(false)
                .isReadByArchiveWriter(false)
                .isReadByInquirer(false)
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

        if(archiveInquiry.isAnswered()){
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
     * 문의하기 조회
     */
    public ArchiveInquiryResponseDTO getInquiry(Long memberId, Long inquiryId) {
        ArchiveInquiry archiveInquiry = archiveInquiryReader.getInquiry(memberId, inquiryId);

        return ArchiveInquiryResponseDTO.of(archiveInquiry);
    }

    /**
     * 문의 답변 유무 조회
     */
    public boolean isAnswered(Long archiveInquiryId){
        return archiveInquiryReader.isAnswered(archiveInquiryId);
    }

    /**
     * 내가 보낸 문의 중 가장 최근 거 조회
     */
    public LatestInquiryResponseDTO getLatestInquiry(Long inquirerId) {
        ArchiveInquiry latestInquiry = archiveInquiryReader.getLatestInquiry(inquirerId);

        LatestInquiryResponseDTO response = LatestInquiryResponseDTO.of(latestInquiry);

        return response;
    }

    /**
     * 특정 아카이브에 대해 읽지 않은 문의가 있어?
     */
    public boolean hasUnreadInquiryForArchive(Long archiveId) {
        return archiveInquiryReader.hasUnreadInquiryForArchive(archiveId);
    }
}