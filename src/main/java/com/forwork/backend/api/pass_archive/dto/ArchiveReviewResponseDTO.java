package com.forwork.backend.api.pass_archive.dto;

import com.forwork.backend.api.pass_archive.entity.ArchiveReview;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ArchiveReviewResponseDTO(
        @Schema(description = "리뷰 id")
        Long archiveReviewId,
        @Schema(description = "별점")
        double star,
        @Schema(description = "내용")
        String content,
        @Schema(description = "작성 날짜")
        LocalDateTime createdAt
) {

        public static ArchiveReviewResponseDTO of(ArchiveReview archiveReview) {
                return new ArchiveReviewResponseDTO(
                        archiveReview.getId(),
                        archiveReview.getStar(),
                        archiveReview.getContent(),
                        archiveReview.getCreatedAt()
                );
        }
}
