package com.forwork.backend.api.recruit_review.dto.request;

import com.forwork.backend.api.member.entity.JobCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record RecruitReviewUpdateDTO(
        @Schema(description = "제목")
        String title,
        @Schema(description = "후기 내용")
        String content,
        @Schema(description = "(시/도)")
        String region1,
        @Schema(description = "(시/구/군)")
        String region2,
        @Schema(description = "업직종")
        JobCategory jobCategory
) {
}
