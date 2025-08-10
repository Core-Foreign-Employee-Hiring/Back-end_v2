package com.forwork.backend.api.pass_archive.dto;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import io.swagger.v3.oas.annotations.media.Schema;

public record PassArchivePreviewResponseDTO(
        @Schema(description = "아카이브 id")
        Long passArchiveId,
        @Schema(description = "썸네일 url")
        String thumbnailUrl,
        @Schema(description = "제목")
        String title,
        @Schema(description = "한줄 설명")
        String oneLineReview,
        @Schema(description = "가격")
        double price,
        @Schema(description = "별점")
        double star,
        @Schema(description = "별점 수?")
        long starCount
) {
    public static PassArchivePreviewResponseDTO of(PassArchive passArchive){
        return new PassArchivePreviewResponseDTO(
                passArchive.getPassArchiveId(),
                passArchive.getThumbnail().getFileUrl(),
                passArchive.getTitle(),
                passArchive.getOneLineReview(),
                passArchive.getPrice(),
                passArchive.getStar(),
                passArchive.getStarCount()
        );
    }
}
