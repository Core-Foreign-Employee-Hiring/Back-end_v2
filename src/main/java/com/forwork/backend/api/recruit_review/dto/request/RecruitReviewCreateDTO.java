package com.forwork.backend.api.recruit_review.dto.request;


import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.recruit_review.entity.RecruitReview;
import io.swagger.v3.oas.annotations.media.Schema;

public record RecruitReviewCreateDTO(

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


    public RecruitReview toEntity(Member writer){
        return RecruitReview.builder()
                .title(title)
                .content(content)
                .region1(region1)
                .region2(region2)
                .readCount(0)
                .jobCategory(jobCategory)
                .writer(writer)
                .isDeleted(false)
                .build();
    }
}
