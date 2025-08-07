package com.forwork.backend.api.mypage.dto.response;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PurchasedArchivesPreviewResponseDTO(
        @Schema(description = "아카이브 id")
        Long passArchiveId,
        @Schema(description = "썸네일 사진 url")
        String thumbnailUrl,
        @Schema(description = "제목")
        String title,
        @Schema(description = "한 줄설명")
        String oneLineReview,
        @Schema(description = "가격")
        long price,
        @Schema(description = "결제날짜")
        LocalDate approvedAt,
        @Schema(description = "리뷰 id")
        Long reviewId
        // 별점? 추가할 것
) {

    public static PurchasedArchivesPreviewResponseDTO of(PassArchive passArchive, LocalDate approvedAt){
        String thumbnailUrl=(passArchive.getThumbnail()==null)?null:passArchive.getThumbnail().getFileUrl();

        return new PurchasedArchivesPreviewResponseDTO(
                passArchive.getPassArchiveId(),
                thumbnailUrl,
                passArchive.getTitle(),
                passArchive.getOneLineReview(),
                passArchive.getPrice(),
                approvedAt,
                null
        );
    }
}
