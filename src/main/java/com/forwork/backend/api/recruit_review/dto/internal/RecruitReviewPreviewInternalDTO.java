package com.forwork.backend.api.recruit_review.dto.internal;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit_review.entity.RecruitReview;

import java.time.LocalDateTime;

public record RecruitReviewPreviewInternalDTO(

        Long recruitReviewId,
        JobCategory jobCategory,
        String title,
        String content,
        String region1,
        String region2,
        LocalDateTime createAt,
        long readCount,
        long commentCount
) {

    public static RecruitReviewPreviewInternalDTO of(RecruitReview recruitReview, long commentCount) {
        return new RecruitReviewPreviewInternalDTO(
                recruitReview.getId(),
                recruitReview.getJobCategory(),
                recruitReview.getTitle(),
                recruitReview.getContent(),
                recruitReview.getRegion1(),
                recruitReview.getRegion2(),
                recruitReview.getCreatedAt(),
                recruitReview.getReadCount(),
                commentCount
        );
    }
}
