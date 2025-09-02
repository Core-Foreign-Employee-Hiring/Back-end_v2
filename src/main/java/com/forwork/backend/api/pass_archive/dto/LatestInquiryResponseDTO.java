package com.forwork.backend.api.pass_archive.dto;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.pass_archive.entity.ArchiveInquiry;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import io.swagger.v3.oas.annotations.media.Schema;

public record LatestInquiryResponseDTO(
        @Schema(description = "id")
        Long archiveInquiryId,
        @Schema(description = "")
        String profileImage,
        @Schema(description = "유저명")
        String name,
        @Schema(description = "제목")
        String title,
        @Schema(description = "한 줄설명")
        String oneLineReview,
        @Schema(description = "가격")
        long price,
        @Schema(description = "문의")
        String inquiry,
        @Schema(description = "답변 유무")
        boolean isAnswered,
        @Schema(description = "답변.")
        String answer
) {
    public static LatestInquiryResponseDTO of(ArchiveInquiry archiveInquiry) {
        PassArchive archive = archiveInquiry.getArchive();
        Member archiveOwner = archive.getMember();

        return new LatestInquiryResponseDTO(
                archiveInquiry.getId(),
                archiveOwner.getProfileImage(),
                archiveOwner.getName(),
                archive.getTitle(),
                archive.getOneLineReview(),
                archive.getPrice(),
                archiveInquiry.getInquiry(),
                archiveInquiry.isAnswered(),
                archiveInquiry.getAnswer()
        );
    }
}
