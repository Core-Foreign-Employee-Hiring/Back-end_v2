package com.forwork.backend.api.pass_archive.repository;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.forwork.backend.api.pass_archive.entity.QPassArchive.passArchive;

public class PassArchiveRepositoryImpl implements PassArchiveQueryDSL{
    private final JPAQueryFactory queryFactory;

    public PassArchiveRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PassArchive> getPassArchives(String keyword, Pageable pageable) {
        // 아키이브 조회
        List<PassArchive> content = queryFactory
                .select(passArchive)
                .from(passArchive)
                .join(passArchive.thumbnail).fetchJoin()
                .where(
                        keywordEq(keyword)
                )
                .orderBy(passArchive.passArchiveId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(passArchive.count())
                .from(passArchive)
                .join(passArchive.thumbnail)
                .where(
                        keywordEq(keyword)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }

    private BooleanExpression keywordEq(String keyword) {
        return (keyword == null || keyword.isEmpty()) ? null
                : passArchive.title.contains(keyword).or(passArchive.description.contains(keyword))
                .or(passArchive.oneLineReview.contains(keyword));
    }
}
