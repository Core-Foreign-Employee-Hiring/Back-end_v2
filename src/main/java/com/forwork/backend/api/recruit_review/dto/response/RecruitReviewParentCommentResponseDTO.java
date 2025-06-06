package com.forwork.backend.api.recruit_review.dto.response;

import com.forwork.backend.api.recruit_review.entity.RecruitReviewComment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record RecruitReviewParentCommentResponseDTO(
        @Schema(description = "댓글 id")
        Long commentId,
        @Schema(description = "댓글 내용")
        String comment,
        @Schema(description = "유저 로그인 id")
        String userId,
        @Schema(description = "댓글 작성 날짜")
        LocalDate createdAt,

        @Schema(description = "대댓글들")
        List<RecruitReviewChildCommentResponseDTO> childComments,

        @Schema(description = "내 댓글?")
        boolean isMine

) {

    public static RecruitReviewParentCommentResponseDTO of(RecruitReviewComment comment, Long memberId,
                                                           List<RecruitReviewChildCommentResponseDTO> childComments){

        return new RecruitReviewParentCommentResponseDTO(
                comment.getId(),
                comment.getComment(),
                (comment.isDeleted() || comment.isDeletedButHasChild())?"(삭제)":comment.getWriter().getUserId(),
                comment.getCreatedAt().toLocalDate(),
                childComments,
                Objects.equals(memberId, comment.getWriter().getId())
        );
    }
}
