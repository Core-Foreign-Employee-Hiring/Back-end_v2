package com.forwork.backend.api.recruit_review.repository;

import com.forwork.backend.api.recruit_review.dto.internal.RecruitReviewPreviewInternalDTO;
import com.forwork.backend.api.recruit_review.dto.query.RecruitReviewCommentCountQueryDTO;
import com.forwork.backend.api.recruit_review.entity.RecruitReview;
import com.forwork.backend.api.recruit_review.enums.RecruitReviewSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.forwork.backend.api.recruit_review.entity.QRecruitReview.recruitReview;
import static com.forwork.backend.api.recruit_review.entity.QRecruitReviewComment.recruitReviewComment;

public class RecruitReviewRepositoryImpl implements RecruitReviewRepositoryQueryDSL{
    private final JPAQueryFactory queryFactory;

    public RecruitReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RecruitReviewPreviewInternalDTO> getRecruitPreviews(String keyword, Pageable pageable, RecruitReviewSortType sortType) {

        // 후기 조회
        List<RecruitReview> recruitReviews = queryFactory
                .selectFrom(recruitReview)
                .where(
                        recruitReview.isDeleted.isFalse(),
                        keywordEq(keyword)
                )
                .orderBy(createOrderSpecifier(sortType).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 없으면 바로 return
        if(recruitReviews.isEmpty()){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // 후기 ids 추출
        List<Long> recruitReviewIds = recruitReviews.stream().map(RecruitReview::getId).collect(Collectors.toList());

        // 댓글 수 조회
        List<RecruitReviewCommentCountQueryDTO> commentCounts = queryFactory
                .select(Projections.constructor(
                        RecruitReviewCommentCountQueryDTO.class,
                        recruitReviewComment.recruitReview.id,
                        recruitReviewComment.count()
                ))
                .from(recruitReviewComment)
                .where(
                        recruitReviewComment.isDeleted.isFalse(),
                        recruitReviewComment.recruitReview.id.in(recruitReviewIds)
                )
                .groupBy(recruitReviewComment.recruitReview.id)
                .fetch();


        // return type 으로 변환
        List<RecruitReviewPreviewInternalDTO> content = convertToPreviewInternalDTO(recruitReviews, commentCounts);


        JPAQuery<Long> countQuery=queryFactory
                .select(recruitReview.count())
                .from(recruitReview)
                .where(keywordEq(keyword));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }

    private BooleanExpression keywordEq(String keyword){
        return (keyword == null || keyword.isEmpty())?null
                : recruitReview.title.contains(keyword).or(recruitReview.content.contains(keyword));
    }

    // 동적 정렬
    private List<OrderSpecifier<?>> createOrderSpecifier(RecruitReviewSortType sortType) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortType.equals(RecruitReviewSortType.MOST_VIEWED)) {
            orderSpecifiers.add(new OrderSpecifier<>(sortType.getOrder(), recruitReview.readCount));
        }

        orderSpecifiers.add(new OrderSpecifier<>(sortType.getOrder(), recruitReview.id));

        return orderSpecifiers;
    }

    // PreviewInternalDTO 로 변환.
    private List<RecruitReviewPreviewInternalDTO> convertToPreviewInternalDTO(List<RecruitReview> recruitReviews,
                                                                              List<RecruitReviewCommentCountQueryDTO> commentCounts){

        // key: 후기 id, value: 댓글 수
        Map<Long, Long> commentCountMap = commentCounts.stream()
                .collect(Collectors.toMap(
                        RecruitReviewCommentCountQueryDTO::recruitReviewId,
                        RecruitReviewCommentCountQueryDTO::commentCount
                ));

        List<RecruitReviewPreviewInternalDTO> content = recruitReviews.stream()
                .map(rr -> RecruitReviewPreviewInternalDTO.of(
                        rr,
                        commentCountMap.getOrDefault(rr.getId(), 0L)
                ))
                .collect(Collectors.toList());

        return content;
    }



}
