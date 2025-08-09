package com.forwork.backend.api.pass_archive.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ArchiveInquiryAnswerRequestDTO(
        @Schema(description = "답변 내용")
        String answer
) {
}
