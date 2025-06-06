package com.forwork.backend.api.recruit_review.dto.query;

public record RecruitReviewChildCommentStatQueryDTO(
        long childCount,
        long deletedCount
) {
}
