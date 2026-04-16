package com.forwork.backend.api.file.service;


import com.forwork.backend.api.aws.service.S3Service;
import com.forwork.backend.api.file.FileDirAndName;
import com.forwork.backend.api.file.entity.UploadFile;
import com.forwork.backend.api.file.repository.UploadFileRepository;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.common.exception.InternalServerException;
import com.forwork.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final S3Service s3Service;
    private final UploadFileRepository uploadFileRepository;

    public String uploadOnlyS3(MultipartFile file, FileDirAndName fileDirAndName) {
        String url = null;
        String originalFilename=null;
        if (file != null && !file.isEmpty()) {
            try {
                // S3에 업로드.
                url = s3Service.uploadFile(file, fileDirAndName);

                // DB 저장
                 originalFilename = file.getOriginalFilename();

                UploadFile uploadFile = new UploadFile(url, originalFilename);

                uploadFileRepository.save(uploadFile);


            } catch (IOException e) {
                throw new InternalServerException(ErrorStatus.FAIL_UPLOAD_EXCEPTION.getMessage());
            }
        }

        return url;
    }

    public UploadFile uploadAndSave(MultipartFile file, FileDirAndName dirAndName) {
        try {
            // S3에 업로드
            String url = s3Service.uploadFile(file, dirAndName);

            // UploadFile 엔티티 생성 & 저장
            UploadFile uf = new UploadFile(url, file.getOriginalFilename());
            return uploadFileRepository.save(uf);

        } catch (IOException e) {
            log.error("파일 업로드 또는 저장 중 에러", e);
            throw new InternalServerException(ErrorStatus.FAIL_UPLOAD_EXCEPTION.getMessage());
        }
    }

    public UploadFile saveUrl(String fileUrl) {
        String trimmedFileUrl = fileUrl.trim();
        UploadFile uploadFile = new UploadFile(trimmedFileUrl, extractOriginalFileName(trimmedFileUrl));
        return uploadFileRepository.save(uploadFile);
    }

    private String extractOriginalFileName(String fileUrl) {
        String urlWithoutQuery = fileUrl.split("[?#]", 2)[0];
        int lastSlashIndex = urlWithoutQuery.lastIndexOf('/');

        if (lastSlashIndex < 0 || lastSlashIndex == urlWithoutQuery.length() - 1) {
            return null;
        }

        return urlWithoutQuery.substring(lastSlashIndex + 1);
    }

}
