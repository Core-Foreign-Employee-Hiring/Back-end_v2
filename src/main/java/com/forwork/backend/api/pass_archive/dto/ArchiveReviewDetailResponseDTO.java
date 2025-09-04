package com.forwork.backend.api.pass_archive.dto;

import com.forwork.backend.api.pass_archive.entity.ArchiveReview;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ArchiveReviewDetailResponseDTO(
        @Schema(description = "내용")
        String content,
        @Schema(description = "별점")
        double star,
        @Schema(description = "작성시간")
        LocalDateTime createAt
) {

    public static ArchiveReviewDetailResponseDTO of (ArchiveReview review){
        return new ArchiveReviewDetailResponseDTO(
                review.getContent(),
                review.getStar(),
                review.getCreatedAt()
        );
    }
}
