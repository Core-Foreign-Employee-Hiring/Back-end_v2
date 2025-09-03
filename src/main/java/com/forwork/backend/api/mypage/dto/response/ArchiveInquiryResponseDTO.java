package com.forwork.backend.api.mypage.dto.response;

import com.forwork.backend.api.member.entity.Member;
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
        @Schema(description = "읽음 유무")
        boolean isRead
) {

    public static ArchiveInquiryResponseDTO of(Long memberId, ArchiveInquiry archiveInquiry) {
        Member inquirer = archiveInquiry.getInquirer();
        Member writer = archiveInquiry.getArchive().getMember();
        boolean isRead=false;

        if(inquirer.getId().equals(memberId)){isRead = archiveInquiry.isReadByInquirer();}
        else if(writer.getId().equals(memberId)){isRead= archiveInquiry.isReadByArchiveWriter();}

        return new ArchiveInquiryResponseDTO(
                archiveInquiry.getId(),
                archiveInquiry.getArchive().getTitle(),
                archiveInquiry.getInquiry(),
                archiveInquiry.isAnswered(),
                isRead
        );
    }
}
