package com.forwork.backend.api.recruit_review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RecruitReviewTotalCountResponseDTO(
        @Schema(description = "후기 totalCount")
        Long totalCount
){
}
