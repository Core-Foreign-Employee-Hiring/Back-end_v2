package com.forwork.backend.api.pass_archive.dto;

import com.forwork.backend.api.pass_archive.entity.ArchiveInquiry;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import io.swagger.v3.oas.annotations.media.Schema;

public record ArchiveInquiryResponseDTO(
        @Schema(description = "가격")
        long price,
        @Schema(description = "제목")
        String title,
        @Schema(description = "한줄 설명")
        String oneLineReview,
        @Schema(description = "문의 내용")
        String inquiry,
        @Schema(description = "답변 유무")
        boolean isAnswered,
        @Schema(description = "답변 내용")
        String answer
) {
        public static ArchiveInquiryResponseDTO of(ArchiveInquiry inquiry){
                PassArchive archive = inquiry.getArchive();
                return new ArchiveInquiryResponseDTO(
                        archive.getPrice(),
                        archive.getTitle(),
                        archive.getOneLineReview(),
                        inquiry.getInquiry(),
                        inquiry.isAnswered(),
                        inquiry.getAnswer()
                );
        }
}
