package com.forwork.backend.api.recruit_review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RecruitReviewToTalCountResponseDTO (
        @Schema(description = "후기 totalCount")
        Long totalCount
){
}
