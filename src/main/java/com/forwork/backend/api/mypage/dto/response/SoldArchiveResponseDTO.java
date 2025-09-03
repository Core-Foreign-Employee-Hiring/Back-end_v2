package com.forwork.backend.api.mypage.dto.response;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.forwork.backend.api.pay.entity.Payment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record SoldArchiveResponseDTO(
        @Schema(description = "아카이브 id")
        Long archiveId,
        @Schema(description = "제목")
        String title,
        @Schema(description = "한줄설명")
        String oneLineReview,
        @Schema(description = "가격")
        long price,
        @Schema(description = "결제 날짜")
        LocalDate soldAt,
        @Schema(description = "인출 완료 여부")
        boolean isWithdrawn,
        @Schema(description = "인출 날짜")
        LocalDate withdrawalAt
) {

        public static SoldArchiveResponseDTO of(Payment payment, PassArchive passArchive) {
                return new SoldArchiveResponseDTO(
                        passArchive.getPassArchiveId(),
                        passArchive.getTitle(),
                        passArchive.getOneLineReview(),
                        passArchive.getPrice(),
                        payment.getApprovedAt().toLocalDate(),
                        false, // 임시
                        LocalDate.now() // 임시

                );
        }

}

