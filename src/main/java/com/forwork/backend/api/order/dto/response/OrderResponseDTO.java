package com.forwork.backend.api.order.dto.response;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.order.entity.Order;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import io.swagger.v3.oas.annotations.media.Schema;

public record OrderResponseDTO(
        @Schema(description = "썸네일")
        String thumbnailUrl,

        @Schema(description = "제목")
        String title,

        @Schema(description = "한줄설명")
        String oneLineReview,

        @Schema(description = "수량(1개로 고정)")
        Long quantity,

        @Schema(description = "가격")
        String amount,

        @Schema(description = "이름")
        String name,

        @Schema(description = "이메일")
        String email

) {
        public static OrderResponseDTO of(Member buyer, Order order, PassArchive passArchive) {
                String thumbnailUrl = passArchive.getThumbnail() == null ? null : passArchive.getThumbnail().getFileUrl();
                return new OrderResponseDTO(
                        thumbnailUrl,
                        passArchive.getTitle(),
                        passArchive.getOneLineReview(),
                        1L,
                        order.getAmount(),
                        buyer.getName(),
                        buyer.getEmail()

                );
        }
}