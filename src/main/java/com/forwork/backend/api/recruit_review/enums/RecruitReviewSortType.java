package com.forwork.backend.api.recruit_review.enums;

import com.querydsl.core.types.Order;
import lombok.Getter;

@Getter
public enum RecruitReviewSortType {
    LATEST(Order.DESC),
    MOST_VIEWED(Order.DESC);

    private Order order;

    RecruitReviewSortType(Order order) {
        this.order = order;
    }


}
