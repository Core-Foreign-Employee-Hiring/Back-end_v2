package com.forwork.backend.api.recruit_review.dto.response;

import com.forwork.backend.api.recruit_review.entity.RecruitReviewComment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Objects;

public record RecruitReviewChildCommentResponseDTO(
        @Schema(description = "댓글 id")
        Long commentId,
        @Schema(description = "유저 로그인 id")
        String userId,
        @Schema(description = "댓글 내용")
        String comment,
        @Schema(description = "댓글 작성 날짜")
        LocalDate createdAt,
        @Schema(description = "내 댓글?")
        boolean isMain
) {


    public static  RecruitReviewChildCommentResponseDTO of(RecruitReviewComment comment, Long memberId){

        return new RecruitReviewChildCommentResponseDTO(
                comment.getId(),
                (comment.isDeleted())?"(삭제)":comment.getWriter().getUserId(),
                comment.getComment(),
                comment.getCreatedAt().toLocalDate(),
                Objects.equals(memberId, comment.getWriter().getId())
        );
    }

}
