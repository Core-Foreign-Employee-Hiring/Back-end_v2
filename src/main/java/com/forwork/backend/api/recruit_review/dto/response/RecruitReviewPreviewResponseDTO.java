package com.forwork.backend.api.recruit_review.dto.response;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit_review.dto.internal.RecruitReviewPreviewInternalDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record RecruitReviewPreviewResponseDTO(

        @Schema(description = "후기 id")
        Long recruitReviewId,
        @Schema(description = "업직종")
        JobCategory jobCategory,
        @Schema(description = "제목")
        String title,
        @Schema(description = "후기 내용")
        String content,
        @Schema(description = "(시/도)")
        String region1,
        @Schema(description = "(시/구/군)")
        String region2,
        @Schema(description = "작성 날짜")
        LocalDateTime createAt,
        @Schema(description = "조회수")
        long readCount,
        @Schema(description = "댓글수")
        long commentCount

) {
        public static RecruitReviewPreviewResponseDTO of(RecruitReviewPreviewInternalDTO dto){
                return new RecruitReviewPreviewResponseDTO(
                        dto.recruitReviewId(),
                        dto.jobCategory(),
                        dto.title(),
                        dto.content(),
                        dto.region1(),
                        dto.region2(),
                        dto.createAt(),
                        dto.readCount(),
                        dto.commentCount()
                );
        }
}
