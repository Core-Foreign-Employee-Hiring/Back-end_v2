package com.forwork.backend.api.recruit.dto.response;

import com.forwork.backend.api.recruit.enums.RecruitBookmarkStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record RecruitBookmarkStatusResponseDTO(
        @Schema(description = "현재 북마크 상태")
        RecruitBookmarkStatus recruitBookmarkStatus
) {
}
