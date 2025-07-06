package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.api.recruit.enums.RecruitPublishStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.forwork.backend.api.recruit.entity.QJobCategoryEntity.jobCategoryEntity;
import static com.forwork.backend.api.recruit.entity.QRecruit.recruit;
import static com.forwork.backend.api.recruit.entity.QRecruitJobCategory.recruitJobCategory;

public class RecruitRepositoryImpl implements RecruitRepositoryQueryDSL {
    private final JPAQueryFactory queryFactory;

    public RecruitRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Recruit> getRecruits(String keyword, List<JobCategory> jobCategories, List<ContractType> contractTypes, Pageable pageable) {

        LocalDate now = LocalDate.now();

        List<Long> ids=queryFactory.
                selectDistinct(recruit.id)
                .from(recruit)
                .leftJoin(recruit.recruitJobCategories, recruitJobCategory)
                .where(
                        recruit.recruitPublishStatus.eq(RecruitPublishStatus.PUBLISHED),
                        recruit.recruitEndDate.goe(now),
                        keywordEq(keyword),
                        jobCategoryEq(jobCategories),
                        contractTypeEq(contractTypes)
                )
                .orderBy(recruit.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if(ids.isEmpty()){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<Recruit> content=queryFactory
                .selectFrom(recruit)
                .leftJoin(recruit.recruitJobCategories, recruitJobCategory).fetchJoin()
                .leftJoin(recruitJobCategory.jobCategoryEntity, jobCategoryEntity).fetchJoin()
                .where(recruit.id.in(ids))
                .orderBy(recruit.id.desc())
                .fetch();

        JPAQuery<Long> countQuery=queryFactory
                .select(recruit.count())
                .from(recruit)
                .where(
                        recruit.recruitPublishStatus.eq(RecruitPublishStatus.PUBLISHED),
                        recruit.recruitEndDate.goe(now),
                        keywordEq(keyword),
                        jobCategoryEq(jobCategories),
                        contractTypeEq(contractTypes)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }


    private BooleanExpression keywordEq(String keyword){
        return (keyword == null || keyword.isEmpty())?null
                : recruit.companyName.contains(keyword);
    }

    private BooleanExpression jobCategoryEq(List<JobCategory> jobCategories) {
        if(jobCategories==null || jobCategories.isEmpty()){return null;}

        List<Long> jobCategoryIds = queryFactory
                .select(jobCategoryEntity.id)
                .from(jobCategoryEntity)
                .where(jobCategoryEntity.jobCategory.in(jobCategories))
                .fetch();

        return recruitJobCategory.recruit.id.eq(recruit.id).and(recruitJobCategory.jobCategoryEntity.id.in(jobCategoryIds));
    }

    private BooleanExpression contractTypeEq(List<ContractType> contractTypes){
        if(contractTypes==null || contractTypes.isEmpty()){return null;}

        return recruit.contractType.in(contractTypes);
    }

}
