package com.forwork.backend.api.recruit_review.dto.response;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.recruit_review.entity.RecruitReview;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record RecruitReviewDetailResponseDTO(
        @Schema(description = "채용  후기 id")
        Long recruitReviewId,
        @Schema(description = "(시/도)")
        String region1,
        @Schema(description = "(시/구/군)")
        String region2,
        @Schema(description = "후기 제목")
        String title,
        @Schema(description = "조회수")
        long readCount,
        @Schema(description = "댓글수")
        long commentCount,
        @Schema(description = "후기 내용")
        String content,
        @Schema(description = "작성자(회원의 userId)")
        String userId,
        @Schema(description = "현재 로그인한 사용자가 작성한 후기인지 여부")
        boolean isMine,
        @Schema(description = "생성일자")
        LocalDate createdAt,
        @Schema(description = "업직종")
        JobCategory jobCategory
) {

        public static RecruitReviewDetailResponseDTO of(RecruitReview review, Long commentCount, boolean isMine, Member writer) {
                return new RecruitReviewDetailResponseDTO(
                        review.getId(),
                        review.getRegion1(),
                        review.getRegion2(),
                        review.getTitle(),
                        review.getReadCount()+1,
                        commentCount,
                        review.getContent(),
                        writer.getUserId(),
                        isMine,
                        review.getCreatedAt().toLocalDate(),
                        review.getJobCategory()
                );
        }
}
