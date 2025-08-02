package com.forwork.backend.api.pass_archive.service;

import com.forwork.backend.api.file.FileDirAndName;
import com.forwork.backend.api.file.entity.UploadFile;
import com.forwork.backend.api.file.service.FileService;
import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.pass_archive.dto.PassArchiveCreateRequestDTO;
import com.forwork.backend.api.pass_archive.dto.PassArchiveDetailResponseDTO;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
import com.forwork.backend.common.exception.NotFoundException;
import com.forwork.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassArchiveService {

    private final FileService fileService;
    private final PassArchiveSaveService passArchiveSaveService;
    private final MemberRepository memberRepository;
    private final PassArchiveRepository passArchiveRepository;

    // 합격 아카이브 생성(파일 업로드 후 저장 메소드 전달)
    public Long createArchive(PassArchiveCreateRequestDTO passArchiveCreateRequestDTO, MultipartFile thumbnail, List<MultipartFile> images, List<MultipartFile> products, Long memberId) {

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

        // 상품 파일 저장
        List<UploadFile> productFiles = Optional.ofNullable(products)
                .orElse(Collections.emptyList())
                .stream()
                .map(f -> fileService.uploadAndSave(f, FileDirAndName.File))
                .collect(Collectors.toList());

        return passArchiveSaveService.saveArchive(passArchiveCreateRequestDTO, member, thumbFile, imageFiles, productFiles);
    }

    // 합격 아카이브 상세 조회
    @Transactional(readOnly = true)
    public PassArchiveDetailResponseDTO getDetailArchive(Long passArchiveId) {

        PassArchive passArchive = passArchiveRepository.findWithThumbnailImagesMemberByPassArchiveId(passArchiveId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PASS_ARCHIVE_NOT_FOUND_EXCEPTION.getMessage()));

        return PassArchiveDetailResponseDTO.from(passArchive);
    }
}