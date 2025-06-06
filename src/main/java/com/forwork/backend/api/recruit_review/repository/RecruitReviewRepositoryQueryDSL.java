package com.forwork.backend.api.recruit_review.repository;

import com.forwork.backend.api.recruit_review.dto.internal.RecruitReviewPreviewInternalDTO;
import com.forwork.backend.api.recruit_review.enums.RecruitReviewSortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitReviewRepositoryQueryDSL {

    Page<RecruitReviewPreviewInternalDTO> getRecruitPreviews(String keyword, Pageable pageable, RecruitReviewSortType sortType);
}
