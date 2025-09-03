package com.forwork.backend.api.mypage.dto.response;

import com.forwork.backend.api.pass_archive.entity.ArchiveInquiry;
import io.swagger.v3.oas.annotations.media.Schema;

public record ArchiveInquiryResponseDTO(
        @Schema(description = "문의 id")
        Long archiveInquiryId,
        @Schema(description = "제목")
        String title,
        @Schema(description = "문의")
        String inquiry,
        @Schema(description = "답변 유무")
        boolean isAnswered,
        @Schema(description = "답변.")
        String answer
) {

    public static ArchiveInquiryResponseDTO of(ArchiveInquiry archiveInquiry) {
        return new ArchiveInquiryResponseDTO(
                archiveInquiry.getId(),
                archiveInquiry.getArchive().getTitle(),
                archiveInquiry.getInquiry(),
                archiveInquiry.isAnswered(),
                archiveInquiry.getAnswer()
        );
    }
}
