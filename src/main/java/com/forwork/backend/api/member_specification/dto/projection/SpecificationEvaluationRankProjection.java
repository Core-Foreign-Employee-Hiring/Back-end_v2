package com.forwork.backend.api.member_specification.dto.projection;

public interface SpecificationEvaluationRankProjection {
    Integer getScore();    // 스펙 점수
    Long getCount();       // 자신보다 높은 점수 개수
}

