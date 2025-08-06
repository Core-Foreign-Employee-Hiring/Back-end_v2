package com.forwork.backend.api.pass_archive.dto;

import com.forwork.backend.api.file.entity.UploadFile;
import io.swagger.v3.oas.annotations.media.Schema;

public record PassArchiveFileResponseDTO(
        @Schema(description = "url")
        String fileUrl,
        @Schema(description = "원본 이름")
        String originalFileName
) {


    public static PassArchiveFileResponseDTO of(UploadFile uploadFile) {
        return new PassArchiveFileResponseDTO(
                uploadFile.getFileUrl(),
                uploadFile.getOriginalFileName()
        );
    }
}
