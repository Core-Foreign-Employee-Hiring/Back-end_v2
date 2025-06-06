package com.forwork.backend.api.recruit_review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record RecruitReviewCommentUpdateDTO(
        @Schema(description = "댓글 내용")
        String comment
) {
}
