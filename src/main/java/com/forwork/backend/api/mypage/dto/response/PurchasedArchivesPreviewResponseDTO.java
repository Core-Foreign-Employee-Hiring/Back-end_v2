package com.forwork.backend.api.mypage.dto.response;

import com.forwork.backend.api.pass_archive.entity.ArchiveReview;
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
        @Schema(description = "한줄설명")
        String oneLineReview,
        @Schema(description = "가격")
        long price,
        @Schema(description = "결제날짜")
        LocalDate approvedAt,
        @Schema(description = "리뷰 작성 유무")
        boolean isReviewed,
        @Schema(description = "리뷰 id")
        Long archiveReviewId,
        @Schema(description = "별점")
        double star
) {

    public static PurchasedArchivesPreviewResponseDTO of(PassArchive passArchive, ArchiveReview archiveReview, LocalDate approvedAt){
        String thumbnailUrl=(passArchive.getThumbnail()==null)?null:passArchive.getThumbnail().getFileUrl();
        boolean isReviewed= archiveReview != null;
        Long archiveReviewId=(archiveReview==null)?null:archiveReview.getId();
        double start=(archiveReview==null)?0.0:archiveReview.getStar();

        return new PurchasedArchivesPreviewResponseDTO(
                passArchive.getPassArchiveId(),
                thumbnailUrl,
                passArchive.getTitle(),
                passArchive.getOneLineReview(),
                passArchive.getPrice(),
                approvedAt,
                isReviewed,
                archiveReviewId,
                start
        );
    }
}
