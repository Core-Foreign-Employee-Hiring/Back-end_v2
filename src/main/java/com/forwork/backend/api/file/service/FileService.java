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

}
