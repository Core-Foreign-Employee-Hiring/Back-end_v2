package com.forwork.backend.api.pass_archive.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record ArchiveReviewRequestDTO(
        @Schema(description = "별점")
        double star,
        @Schema(description = "내용")
        String content
) {
}
