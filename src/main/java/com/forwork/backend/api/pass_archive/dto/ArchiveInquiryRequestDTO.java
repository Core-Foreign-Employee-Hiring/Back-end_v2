package com.forwork.backend.api.pass_archive.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ArchiveInquiryRequestDTO(
        @Schema(description = "문의 내용")
        String inquiry
) {
}
