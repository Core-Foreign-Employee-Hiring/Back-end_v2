package com.forwork.backend.api.pass_archive.dto;

import com.forwork.backend.api.pass_archive.entity.ArchiveInquiry;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import io.swagger.v3.oas.annotations.media.Schema;

public record ArchiveInquiryAnswerResponseDTO(
        @Schema(description = "가격")
        long price,
        @Schema(description = "제목")
        String title,
        @Schema(description = "한줄 설명")
        String oneLineReview,
        @Schema(description = "문의 내용")
        String inquiry,
        @Schema(description = "답변 내용")
        String answer
) {
        public static ArchiveInquiryAnswerResponseDTO of(ArchiveInquiry inquiry){
                PassArchive archive = inquiry.getArchive();
                return new ArchiveInquiryAnswerResponseDTO(
                        archive.getPrice(),
                        archive.getTitle(),
                        archive.getOneLineReview(),
                        inquiry.getInquiry(),
                        inquiry.getAnswer()
                );
        }
}
