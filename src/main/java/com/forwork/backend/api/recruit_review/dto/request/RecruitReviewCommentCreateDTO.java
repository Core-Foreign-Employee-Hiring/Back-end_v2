package com.forwork.backend.api.recruit_review.dto.request;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.recruit_review.entity.RecruitReview;
import com.forwork.backend.api.recruit_review.entity.RecruitReviewComment;
import io.swagger.v3.oas.annotations.media.Schema;

public record RecruitReviewCommentCreateDTO(
        @Schema(description = "댓글 내용")
        String comment,
        @Schema(description = "부모 댓글 id",
                example = "댓글일 경우: 빈 문자열, 대댓글일 경우: 부모 댓글 id")
        Long parentId
) {


    public RecruitReviewComment toEntity(Member writer, RecruitReviewComment parentComment, RecruitReview recruitReview) {
        return RecruitReviewComment.builder()
                .comment(comment)
                .writer(writer)
                .recruitReview(recruitReview)
                .parent(parentComment)
                .isDeleted(false)
                .isDeletedButHasChild(false)
                .build();
    }
}
