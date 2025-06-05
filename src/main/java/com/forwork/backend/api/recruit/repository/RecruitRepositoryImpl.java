package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.RecruitPublishStatus;
import com.forwork.backend.api.recruit.enums.SalaryType;
import com.forwork.backend.api.recruit.enums.WorkDayType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Collections;
import java.util.List;

import static com.forwork.backend.api.member.entity.QEmployer.employer;
import static com.forwork.backend.api.recruit.entity.QJobCategoryEntity.jobCategoryEntity;
import static com.forwork.backend.api.recruit.entity.QRecruit.recruit;
import static com.forwork.backend.api.recruit.entity.QRecruitJobCategory.recruitJobCategory;

public class RecruitRepositoryImpl implements RecruitRepositoryQueryDSL {
    private final JPAQueryFactory queryFactory;

    public RecruitRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Recruit> getRecruits(String keyword, List<JobCategory> jobCategories, List<WorkDayType> workDayType,
                                     String workStartTime, String workEndTime, List<SalaryType> salaryType,
                                     Pageable pageable) {
        List<Long> ids=queryFactory.
                selectDistinct(recruit.id)
                .from(recruit, recruitJobCategory, employer)
                .where(
                        recruit.recruitPublishStatus.eq(RecruitPublishStatus.PUBLISHED),
                        keywordEq(keyword),
                        jobCategoryEq(jobCategories),
                        workDayEq(workDayType),
                        workStartTimeEq(workStartTime),
                        workEndTimeEq(workEndTime),
                        salaryTypeEq(salaryType)
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
                .innerJoin(recruit.employer).fetchJoin()
                .where(recruit.id.in(ids))
                .orderBy(recruit.id.desc())
                .fetch();

        JPAQuery<Long> countQuery=queryFactory
                .select(recruit.count())
                .from(recruit)
                .where(
                        keywordEq(keyword),
                        jobCategoryEq(jobCategories),
                        workDayEq(workDayType),
                        workStartTimeEq(workStartTime),
                        workEndTimeEq(workEndTime),
                        salaryTypeEq(salaryType)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }


    private BooleanExpression keywordEq(String keyword){
        return (keyword == null || keyword.isEmpty())?null
                : employer.id.eq(recruit.employer.id).and(employer.companyName.contains(keyword).or(recruit.title.contains(keyword)));
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

    private BooleanExpression workDayEq(List<WorkDayType> workDayType) {
        return (workDayType == null || workDayType.isEmpty())?null
                :recruit.workDayType.in(workDayType);
    }
    private BooleanExpression workStartTimeEq(String workStartTime) {
        return (workStartTime==null)?null:recruit.workStartTime.eq(Recruit.parseTimeStringToInt(workStartTime));
    }

    private BooleanExpression workEndTimeEq(String workEndTime) {
        return (workEndTime==null)?null:recruit.workEndTime.eq(Recruit.parseTimeStringToInt(workEndTime));
    }

    private BooleanExpression salaryTypeEq(List<SalaryType> salaryType){
        return (salaryType==null || salaryType.isEmpty())?null
                :recruit.salaryType.in(salaryType);
    }

}
