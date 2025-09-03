package com.forwork.backend.api.mypage.dto.response;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import io.swagger.v3.oas.annotations.media.Schema;

public record WrittenArchiveResponseDTO(
        @Schema(description = "아카이브 id")
        Long archiveId,
        @Schema(description = "썸네일 사진 url")
        String thumbnailUrl,
        @Schema(description = "제목")
        String title,
        @Schema(description = "한줄설명")
        String oneLineReview,
        @Schema(description = "가격")
        long price,
        @Schema(description = "판매개수")
        Long salesCount,
        @Schema(description = "별점")
        double star,
        @Schema(description = "별점 수")
        long starCount
) {

    public static WrittenArchiveResponseDTO of(PassArchive passArchive, Long salesCount) {
        String thumbnailUrl=(passArchive.getThumbnail()==null)?null:passArchive.getThumbnail().getFileUrl();
        salesCount=(salesCount==null)?0:salesCount;

        return new WrittenArchiveResponseDTO(
                passArchive.getPassArchiveId(),
                thumbnailUrl,
                passArchive.getTitle(),
                passArchive.getOneLineReview(),
                passArchive.getPrice(),
                salesCount,
                passArchive.getStar(),
                passArchive.getStarCount()
        );
    }
}
