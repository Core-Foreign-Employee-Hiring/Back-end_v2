package com.forwork.backend.api.pass_archive.service;

import com.forwork.backend.api.file.FileDirAndName;
import com.forwork.backend.api.file.entity.UploadFile;
import com.forwork.backend.api.file.service.FileService;
import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.order.repository.OrderRepository;
import com.forwork.backend.api.pass_archive.dto.PassArchiveCreateRequestDTO;
import com.forwork.backend.api.pass_archive.dto.PassArchiveDetailResponseDTO;
import com.forwork.backend.api.pass_archive.dto.PassArchiveFileResponseDTO;
import com.forwork.backend.api.pass_archive.dto.PassArchivePreviewResponseDTO;
import com.forwork.backend.api.pass_archive.entity.ArchiveDownloadHistory;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.ArchiveDownloadHistoryRepository;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.NotFoundException;
import com.forwork.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassArchiveService {

    private final FileService fileService;
    private final PassArchiveSaveService passArchiveSaveService;
    private final MemberRepository memberRepository;
    private final PassArchiveRepository passArchiveRepository;
    private final OrderRepository orderRepository;
    private final ArchiveDownloadHistoryRepository archiveDownloadHistoryRepository;

    // 합격 아카이브 생성
    public Long createArchive(PassArchiveCreateRequestDTO passArchiveCreateRequestDTO, MultipartFile thumbnail, List<MultipartFile> images, Long memberId) {

        // 작성자 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()
                ));

        // 썸네일 저장
        UploadFile thumbFile = fileService.uploadAndSave(thumbnail, FileDirAndName.File);

        // 본문 이미지 저장
        List<UploadFile> imageFiles = Optional.ofNullable(images)
                .orElse(Collections.emptyList())
                .stream()
                .map(f -> fileService.uploadAndSave(f, FileDirAndName.File))
                .collect(Collectors.toList());

        // 상품 URL 저장
        List<UploadFile> productFiles = Optional.ofNullable(passArchiveCreateRequestDTO.getProducts())
                .orElse(Collections.emptyList())
                .stream()
                .filter(url -> url != null && !url.trim().isEmpty())
                .map(fileService::saveUrl)
                .collect(Collectors.toList());

        return passArchiveSaveService.saveArchive(passArchiveCreateRequestDTO, member, thumbFile, imageFiles, productFiles);
    }

    // 합격 아카이브 상세 조회
    @Transactional(readOnly = true)
    public PassArchiveDetailResponseDTO getDetailArchive(Long memberId, Long passArchiveId) {

        PassArchive passArchive = passArchiveRepository.findWithThumbnailImagesMemberByPassArchiveId(passArchiveId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage()));

        if(passArchive.isDeleted()){
            throw new NotFoundException(ErrorStatus.PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage());
        }

        // 작성자인지 판단
        Member writer = passArchive.getMember();
        boolean isWriter= writer.getId().equals(memberId);

        return PassArchiveDetailResponseDTO.from(passArchive, isWriter);
    }

    // 아카이브 다운
    @Transactional
    public List<PassArchiveFileResponseDTO> downloadArchive(Long memberId, Long passArchiveId){
        // 구매했는지
        boolean b = orderRepository.existsPurchasedArchive(memberId, passArchiveId);

        // 구매 안 했으면 예외
        if(!b){throw new BadRequestException(ARCHIVE_PURCHASE_FORBIDDEN_EXCEPTION.getMessage());}

        PassArchive passArchive = passArchiveRepository.findArchiveByArchiveIdWithProducts(passArchiveId)
                .orElseThrow(() -> {
                    log.warn("[downloadArchive][아카이브 not found][passArchiveId= {}]", passArchiveId);
                    return new BadRequestException(ErrorStatus.PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage());
                });

        List<PassArchiveFileResponseDTO> response = passArchive.getProducts().stream()
                .map(PassArchiveFileResponseDTO::of)
                .toList();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[downloadArchive][멤버 없음.][memberId={}]", memberId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        ArchiveDownloadHistory history = ArchiveDownloadHistory.builder()
                .buyer(member)
                .passArchive(passArchive)
                .build();

        archiveDownloadHistoryRepository.save(history);
        
        return response;
    }

    // 합격 아카이브 리스트 조회 및 검색
    public PageResponseDTO<PassArchivePreviewResponseDTO> getPassArchives(String keyword, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<PassArchivePreviewResponseDTO> passArchives = passArchiveRepository.getPassArchives(keyword, pageable)
                .map(PassArchivePreviewResponseDTO::of);

        PageResponseDTO<PassArchivePreviewResponseDTO> response = PageResponseDTO.of(passArchives);

        return response;
    }


    // 문의 url 조회
    public String getInquiryUrl(Long archiveId){
        PassArchive passArchive = passArchiveRepository.findArchiveByPassArchiveIdWithThumbnail(archiveId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage()));

        String inquiryUrl = passArchive.getInquiryUrl();

        if(inquiryUrl == null || inquiryUrl.isEmpty()){
            log.warn("[getInquiryUrl][문의 링크 없음][archiveId= {}]", archiveId);
            throw new BadRequestException(ErrorStatus.INQUIRY_LINK_NOT_FOUND.getMessage());
        }

        return inquiryUrl;
    }

    @Transactional
    public void deleteArchive(Long memberId, Long archiveId){
        // 소유자 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[deleteArchive][멤버 없음.][memberId={}]", memberId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        PassArchive passArchive = passArchiveRepository.findArchiveByArchiveIdWithMember(archiveId)
                .orElseThrow(() -> {
                    log.warn("[deleteArchive][아카이브 not found][archiveId= {}]", archiveId);
                    return new BadRequestException(ErrorStatus.PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage());
                });
        
        if(!member.getId().equals(passArchive.getMember().getId())){
            log.warn("[deleteArchive][소유자 아님.][memberId={}, writerId= {}]", memberId, passArchive.getMember().getId());
            throw new BadRequestException(ARCHIVE_ACCESS_DENIED_EXCEPTION.getMessage());
        }


        // 삭제
        passArchive.delete();
    }

}
