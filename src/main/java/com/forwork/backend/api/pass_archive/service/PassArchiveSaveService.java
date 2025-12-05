package com.forwork.backend.api.pass_archive.service;

import com.forwork.backend.api.file.entity.UploadFile;
import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.pass_archive.dto.PassArchiveCreateRequestDTO;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pass_archive.repository.PassArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassArchiveSaveService {

    private final PassArchiveRepository passArchiveRepository;

    // 합격 아카이브 최종 생성
    @Transactional
    public Long saveArchive(PassArchiveCreateRequestDTO passArchiveCreateRequestDTO, Member member, UploadFile thumbFile, List<UploadFile> imageFiles, List<UploadFile> productFiles) {

        PassArchive passArchive = PassArchive.builder()
                .title(passArchiveCreateRequestDTO.getTitle())
                .oneLineReview(passArchiveCreateRequestDTO.getOneLineReview())
                .description(passArchiveCreateRequestDTO.getDescription())
                .price(passArchiveCreateRequestDTO.getPrice())
                .inquiryUrl(passArchiveCreateRequestDTO.getInquiryUrl())
                .star(0)
                .starCount(0)
                .member(member)
                .thumbnail(thumbFile)
                .images(imageFiles)
                .products(productFiles)
                .isDeleted(false)
                .build();

        passArchiveRepository.save(passArchive);
        return passArchive.getPassArchiveId();
    }
}
